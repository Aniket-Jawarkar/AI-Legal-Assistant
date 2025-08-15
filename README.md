# 🤖 AI Legal Assistant – React + Spring Boot

An **AI-powered legal assistant** that lets you upload documents, preview them, summarize them, and ask AI questions about their contents.

Built with:
- **⚛️ Frontend:** React + Tailwind CSS (Vite)
- **☕ Backend:** Spring Boot (Java) with REST APIs
- **🧠 AI Integration:** AI answer service for Q&A & summarization

---

## 🚀 Features

✅ **Upload Documents** – PDFs, images, or text files  
✅ **Document List & Preview** – Friendly file names + file-type icons  
✅ **Summarization** – AI-generated concise summary of a document  
✅ **Ask Questions** – Query the AI about document content or general legal queries  
✅ **Delete Documents** – Remove unwanted uploads easily  
✅ **Responsive UI** – Clean, mobile-friendly layout  

---

## 📂 Project Structure

### 🖥 Backend (Spring Boot)
| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/documents/upload` | POST | Upload a file |
| `/api/documents` | GET | List all documents |
| `/api/documents/{id}` | GET | Get document details |
| `/api/documents/{id}` | DELETE | Delete a document |
| `/api/ai/ask` | POST | Ask AI a question |
| `/api/ai/summarize` | POST | Summarize a document |

### 🎨 Frontend (React + Tailwind CSS)
- Single Page Application (SPA)
- Document list with file icons
- Summarization & Q&A panel
- Upload form with progress + error handling

---

## 🖼 Screenshots

### 1️⃣ Uploading the File
<img width="1602" alt="Upload Screenshot" src="https://github.com/user-attachments/assets/329a938d-2a22-40e4-81ff-63b4adb7e76c" />

### 2️⃣ Asking Questions
<img width="1602" alt="Ask Questions Screenshot" src="https://github.com/user-attachments/assets/93a2a1e0-cfb5-4be1-8ba7-d0e086b02cfc" />

### 3️⃣ Summarization of Whole Document
<img width="1601" alt="Summarization Screenshot" src="https://github.com/user-attachments/assets/b545e194-ae40-4544-b527-d0f6dfbef4fd" />

---

