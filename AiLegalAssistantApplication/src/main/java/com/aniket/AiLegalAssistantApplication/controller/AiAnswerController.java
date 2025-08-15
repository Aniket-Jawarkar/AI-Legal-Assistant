package com.aniket.AiLegalAssistantApplication.controller;

import com.aniket.AiLegalAssistantApplication.service.AiAnswerService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/ai")
public class AiAnswerController {

    private final AiAnswerService aiAnswerService;

    public AiAnswerController(AiAnswerService aiAnswerService) {
        this.aiAnswerService = aiAnswerService;
    }

    // Ask question about document context
    @GetMapping("/ask")
    public String askQuestion(@RequestParam String query) throws IOException, InterruptedException {
        return aiAnswerService.getAnswer(query);
    }

    @PostMapping("/ask")
    public String askQuestionPost(@RequestBody QueryRequest request) throws IOException, InterruptedException {
        return aiAnswerService.getAnswer(request.query);
    }

    // Summarize document
    @GetMapping("/summarize")
    public String summarizeDocument(@RequestParam String documentId) throws IOException, InterruptedException {
        String prompt = "Please provide a short, plain-English summary of the document with ID: " + documentId;
        return aiAnswerService.getAnswer(prompt);
    }

    @PostMapping("/summarize")
    public String summarizeDocumentPost(@RequestBody SummarizeRequest request) throws IOException, InterruptedException {
        String prompt = "Please provide a short, plain-English summary of the document with ID: " + request.documentId;
        return aiAnswerService.getAnswer(prompt);
    }

    // Request classes for POST endpoints
    public static class QueryRequest {
        public String query;
    }

    public static class SummarizeRequest {
        public String documentId;
    }
}
