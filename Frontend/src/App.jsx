import React, { useEffect, useState, useRef } from "react";

export default function App() {
  const [documents, setDocuments] = useState([]);
  const [selectedDoc, setSelectedDoc] = useState(null); 
  const [uploading, setUploading] = useState(false);
  const [aiResponse, setAiResponse] = useState("");
  const [query, setQuery] = useState("");
  const [error, setError] = useState("");
  const fileInputRef = useRef();

  useEffect(() => {
    loadDocuments();
  }, []);

  async function loadDocuments() {
    setError("");
    try {
      const res = await fetch("/api/documents");
      if (!res.ok) throw new Error(`Failed to load documents (${res.status})`);
      const data = await res.json();
      // We only keep the friendly fields for UI — do not expose raw 'data' blob in lists
      setDocuments(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message || String(err));
    }
  }

  function friendlyName(doc) {
    return doc.fileName || doc.file_name || doc.name || `Uploaded document`;
  }

  function fileTypeLabel(doc) {
    const t = (doc.contentType || doc.content_type || "").toLowerCase();
    if (t.startsWith("image/")) return "Image";
    if (t === "application/pdf") return "PDF";
    if (t.startsWith("text/")) return "Text";
    return (t.split("/")[1] || "File").toUpperCase();
  }

  async function uploadFile(e) {
    e.preventDefault();
    setError("");
    const file = fileInputRef.current.files[0];
    if (!file) return setError("Choose a file to upload.");
    setUploading(true);
    try {
      const form = new FormData();
      form.append("file", file);
      const res = await fetch(`/api/documents/upload`, {
        method: "POST",
        body: form,
      });
      if (!res.ok) throw new Error(`Upload failed (${res.status})`);
      const saved = await res.json();
      // Refresh list and select the uploaded document in a friendly way
      await loadDocuments();
      // try to find the document by filename — fallback to the returned object
      const found = (Array.isArray(documents) ? documents : []).find(d => (d.fileName || d.file_name || d.name) === (saved.fileName || saved.file_name || saved.name)) || saved;
      setSelectedDoc(found);
      fileInputRef.current.value = null;
    } catch (err) {
      setError(err.message || String(err));
    } finally {
      setUploading(false);
    }
  }

  async function removeDocument(doc) {
    setError("");
    if (!doc) return;
    // confirm with user
    if (!confirm(`Delete "${friendlyName(doc)}"?`)) return;
    // we still need the id internally to call delete on backend
    const id = doc.id || doc.documentId || doc.fileId;
    if (!id) return setError("Cannot delete this document (missing id).");
    try {
      const res = await fetch(`/api/documents/${id}`, { method: "DELETE" });
      if (!res.ok) throw new Error(`Delete failed (${res.status})`);
      setSelectedDoc((s) => (s && (s.id === id) ? null : s));
      await loadDocuments();
    } catch (err) {
      setError(err.message || String(err));
    }
  }

  // Summarize: single user-friendly action (POST) — backend expects POST /api/ai/summarize with { documentId }
  async function summarizeSelected() {
    setError("");
    setAiResponse("");
    if (!selectedDoc) return setError("Select a document to summarize.");
    const id = selectedDoc.id || selectedDoc.documentId || selectedDoc.fileId;
    if (!id) return setError("Selected document cannot be summarized (missing id).");
    try {
      const res = await fetch(`/api/ai/summarize`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ documentId: String(id) }),
      });
      if (!res.ok) throw new Error(`Summarize failed (${res.status})`);
      const text = await res.text();
      setAiResponse(text);
    } catch (err) {
      setError(err.message || String(err));
    }
  }

  // Ask AI: single user-friendly POST endpoint
  async function askAi(e) {
    e?.preventDefault();
    setError("");
    setAiResponse("");
    if (!query) return setError("Type your question first.");
    try {
      const res = await fetch(`/api/ai/ask`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ query }),
      });
      if (!res.ok) throw new Error(`AI failed (${res.status})`);
      const text = await res.text();
      setAiResponse(text);
    } catch (err) {
      setError(err.message || String(err));
    }
  }

  // Render a small icon or thumbnail for a document (if image data present, show preview)
  function DocThumbnail({ doc }) {
    const t = (doc.contentType || "").toLowerCase();
    // If server sent raw base64 data for images, show a preview — otherwise show icon
    if (t.startsWith("image/") && doc.data) {
      // some backends return raw base64 in 'data' — guard against very long strings
      const src = `data:${t};base64,${doc.data}`;
      return <img src={src} alt={friendlyName(doc)} className="w-12 h-12 object-cover rounded" />;
    }

    // simple SVG icon by file type
    if (t === "application/pdf") {
      return (
        <div className="w-12 h-12 flex items-center justify-center rounded bg-red-50">
          <svg className="w-6 h-6" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M6 2h7l5 5v13a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2z" stroke="#e11d48" strokeWidth="1.2" strokeLinecap="round" strokeLinejoin="round" />
            <path d="M13 2v6h6" stroke="#e11d48" strokeWidth="1.2" strokeLinecap="round" strokeLinejoin="round" />
          </svg>
        </div>
      );
    }

    return (
      <div className="w-12 h-12 flex items-center justify-center rounded bg-slate-50">
        <svg className="w-6 h-6" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8l-6-6z" stroke="#475569" strokeWidth="1.2" strokeLinecap="round" strokeLinejoin="round" />
        </svg>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-50 text-slate-900">
      <header className="bg-white shadow-sm">
        <div className="max-w-5xl mx-auto px-4 py-4 flex items-center justify-between">
          <div>
            <h1 className="text-lg font-semibold">AI Legal Assistant</h1>
            <div className="text-xs text-slate-500">User-friendly — upload, select, summarize, ask</div>
          </div>
        </div>
      </header>

      <main className="max-w-5xl mx-auto p-4 grid grid-cols-1 md:grid-cols-3 gap-6">
        {/* Documents list + upload */}
        <aside className="md:col-span-1 bg-white p-4 rounded-lg shadow">
          <div className="flex items-center justify-between mb-3">
            <h2 className="font-medium">Your Documents</h2>
            <button onClick={loadDocuments} className="text-sm px-2 py-1 border rounded">Refresh</button>
          </div>

          <div className="space-y-3 max-h-72 overflow-auto">
            {documents.length === 0 && <div className="text-sm text-slate-500">No documents uploaded yet.</div>}

            {documents.map((doc, i) => (
              <button
                key={i}
                className={`w-full flex items-center gap-3 p-2 rounded hover:bg-slate-50 text-left ${selectedDoc === doc ? 'ring-2 ring-indigo-200' : ''}`}
                onClick={() => { setSelectedDoc(doc); setAiResponse(""); setError(""); }}
              >
                <DocThumbnail doc={doc} />
                <div className="flex-1">
                  <div className="font-medium">{friendlyName(doc)}</div>
                  <div className="text-xs text-slate-500">{fileTypeLabel(doc)}</div>
                </div>
                <div className="text-xs text-slate-400">{/* invisible id — not shown */}</div>
              </button>
            ))}
          </div>

          <form onSubmit={uploadFile} className="mt-4">
            <label className="block text-sm font-medium mb-1">Upload a file</label>
            <input ref={fileInputRef} type="file" className="block w-full text-sm text-slate-600" />
            <div className="mt-3 flex gap-2">
              <button type="submit" disabled={uploading} className="px-3 py-2 bg-indigo-600 text-white rounded disabled:opacity-60">{uploading ? 'Uploading...' : 'Upload'}</button>
              {selectedDoc && <button type="button" onClick={() => removeDocument(selectedDoc)} className="px-3 py-2 border rounded">Delete selected</button>}
            </div>
          </form>

          {error && <div className="mt-3 text-sm text-red-600">{error}</div>}
        </aside>

        {/* Main area: selected doc + AI panel */}
        <section className="md:col-span-2 space-y-6">
          <div className="bg-white p-4 rounded-lg shadow">
            <div className="flex items-start justify-between">
              <div>
                <h2 className="text-lg font-medium">Selected Document</h2>
                <div className="text-sm text-slate-500">We only show the friendly name and a small preview/icon.</div>
              </div>
              <div>
                <button onClick={() => { setSelectedDoc(null); setAiResponse(""); }} className="text-sm px-2 py-1 border rounded">Clear</button>
              </div>
            </div>

            <div className="mt-4">
              {!selectedDoc && <div className="text-sm text-slate-500">No document selected.</div>}

              {selectedDoc && (
                <div className="flex gap-4 items-start">
                  <DocThumbnail doc={selectedDoc} />
                  <div className="flex-1">
                    <div className="font-semibold">{friendlyName(selectedDoc)}</div>
                    <div className="text-sm text-slate-500">{fileTypeLabel(selectedDoc)}</div>
                    <div className="mt-3 text-sm text-slate-700 bg-slate-50 p-3 rounded">{selectedDoc.description || selectedDoc.summary || 'No preview available.'}</div>

                    <div className="mt-3 flex gap-2">
                      <button onClick={summarizeSelected} className="px-3 py-2 bg-emerald-600 text-white rounded">Summarize</button>
                      <button onClick={() => { setAiResponse(""); setQuery(''); }} className="px-3 py-2 border rounded">Reset AI</button>
                    </div>
                  </div>
                </div>
              )}
            </div>
          </div>

          <div className="bg-white p-4 rounded-lg shadow">
            <h3 className="font-medium">Ask the Assistant</h3>
            <div className="text-sm text-slate-500 mt-1">Ask questions about a selected document or general questions.</div>

            <form onSubmit={askAi} className="mt-3 grid grid-cols-1 md:grid-cols-4 gap-3">
              <input value={query} onChange={(e) => setQuery(e.target.value)} placeholder={selectedDoc ? `Ask about "${friendlyName(selectedDoc)}"` : 'Type your question...'} className="md:col-span-3 p-2 border rounded w-full" />
              <div className="flex gap-2">
                <button type="submit" className="px-3 py-2 bg-indigo-600 text-white rounded">Ask</button>
                <button type="button" onClick={() => { setQuery(''); setAiResponse(''); }} className="px-3 py-2 border rounded">Clear</button>
              </div>
            </form>

            <div className="mt-4">
              <div className="text-sm font-medium">Assistant Answer</div>
              <div className="mt-2 bg-slate-50 p-3 rounded min-h-[120px] whitespace-pre-wrap">{aiResponse ? <div>{aiResponse}</div> : <div className="text-sm text-slate-400">No answer yet.</div>}</div>
            </div>
          </div>
        </section>
      </main>

      <footer className="max-w-5xl mx-auto p-4 text-xs text-slate-500">Tip: During development use the Vite proxy so the app can call <code>/api/*</code> without CORS changes.</footer>
    </div>
  );
}
