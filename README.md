
AI Legal Assistant – React + Spring Boot

This project is a full-stack AI-powered legal assistant that lets users upload documents, view them, summarize them, and ask AI questions about their contents.
It’s built with:

Frontend: React + Tailwind CSS (Vite)

Backend: Spring Boot (Java) with REST APIs

AI Integration: AI answer service for Q&A and summarization

🚀 Features

Upload Documents – Upload PDFs, images, or text files to the backend.

Document List & Preview – See uploaded documents with friendly names and file-type icons.

Summarization – Generate a concise summary of a selected document using AI.

Ask Questions – Query the AI about the document’s contents or general legal questions.

Delete Documents – Remove unwanted documents from the system.

Responsive UI – Clean, mobile-friendly layout with Tailwind CSS styling.

📂 Project Structure
Backend (Spring Boot)

/api/documents – Manage documents

POST /upload – Upload a file

GET / – List all documents

GET /{id} – Get document details

DELETE /{id} – Delete a document

/api/ai – AI features

POST /ask – Ask a question

POST /summarize – Summarize a document

Frontend (React + Tailwind CSS)

Single Page Application (SPA)

Document list with file icons

Summarization & Q&A interface

Upload form with progress and error handling


 1. Uploading the File
<img width="1920" height="1080" alt="Screenshot (434)" src="https://github.com/user-attachments/assets/23558c5f-fd58-42da-b6d4-699d259098a6" />
 2. Asking Questions
<img width="1920" height="1080" alt="Screenshot (435)" src="https://github.com/user-attachments/assets/a2adcd02-78ee-4a27-be76-39830975d8f1" />
 3. Summarization of whole document
<img width="1920" height="1080" alt="Screenshot (436)" src="https://github.com/user-attachments/assets/a3eb3c59-76df-4178-a246-b577e121ef03" />

