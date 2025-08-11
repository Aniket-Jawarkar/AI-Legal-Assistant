package com.aniket.AiLegalAssistantApplication.controller;
/*
    1. POST /api/documents/upload
    (From Frontend)to send a legal document (PDF, Word, etc.) to the system.
    it should  extracts text, splits it into small chunks, creates AI embeddings
     and stores them in a vector database (FAISS/PgVector).


    2. POST /api/documents/{id}/ask
    We ask a question about a specific uploaded document.
    The system finds the most relevant chunks from the document (using embeddings search)
    and sends them to the AI model (Gemini 2.5 Pro).
    AI responds with a direct answer, citing the document.


    3. POST /api/documents/{id}/summarize
    The AI reads the whole document and returns a short, plain-English summary.


    4. GET /api/documents/{id} — metadata & preview
    Gives you metadata (like name, upload date, file size) and a small text preview of the uploaded document.


    5.GET /api/documents — list (optional pagination)
    Lists all uploaded documents (newest first).
    If you have many files, you can fetch them page by page (pagination).


    6. DELETE /api/documents/{id}
    Removes the uploaded document and its embeddings from storage.


*/


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocumentController {

    @PostMapping("/api/document/upload")
    private void uploadDocument(){

    }
}
