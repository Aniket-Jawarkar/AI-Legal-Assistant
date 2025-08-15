package com.aniket.AiLegalAssistantApplication.service;

import com.aniket.AiLegalAssistantApplication.model.Chunk;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiAnswerService {

    private final SearchService searchService;

    // read API key from application.properties or environment (spring will resolve)
    @Value("${GEMINI_API_KEY:}")
    private String geminiApiKey;

    // use the flash model endpoint and do NOT include key in URL
    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    public AiAnswerService(SearchService searchService) {
        this.searchService = searchService;
    }

    @Transactional
    public String getAnswer(String query) throws IOException, InterruptedException {
        // 0. ensure API key present
        if (geminiApiKey == null || geminiApiKey.isBlank()) {
            return "[Error]: Gemini API key not configured (GEMINI_API_KEY).";
        }

        // 1. Fetch top relevant chunks
        List<Chunk> relevantChunks = searchService.searchRelevantChunks(query, 5);

        // 2. Combine chunks into a single context string
        String context = relevantChunks.stream()
                .map(Chunk::getContent)
                .collect(Collectors.joining("\n\n"));

        // 3. Build prompt for Gemini
        String prompt = "You are a legal document assistant. Based on the provided context, answer the query.\n\n"
                + "Context:\n" + context + "\n\n"
                + "Query: " + query + "\n\n"
                + "Answer:";

        // 4. Build JSON request body
        JSONObject requestJson = new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                .put("parts", new JSONArray()
                                        .put(new JSONObject().put("text", prompt))
                                )
                        )
                );

        // 5. Send request to Gemini API (use header X-goog-api-key)
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GEMINI_API_URL))
                .header("Content-Type", "application/json")
                .header("X-goog-api-key", geminiApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestJson.toString()))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 6. Parse response safely
        JSONObject jsonResponse;
        try {
            jsonResponse = new JSONObject(response.body());
        } catch (Exception e) {
            return "[Error]: Failed to parse Gemini response.";
        }

        // Debug print (optional)
        System.out.println("Gemini API raw response:\n" + jsonResponse.toString(2));

        // Handle API errors
        if (jsonResponse.has("error")) {
            JSONObject error = jsonResponse.getJSONObject("error");
            String message = error.optString("message", "Unknown error");
            int code = error.optInt("code", -1);
            return "[Gemini API Error " + code + "]: " + message;
        }

        // Handle missing candidates
        if (!jsonResponse.has("candidates")) {
            return "[Error]: No candidates found in Gemini API response.";
        }

        JSONArray candidates = jsonResponse.getJSONArray("candidates");
        if (candidates.isEmpty()) {
            return "[Error]: Candidates array is empty.";
        }

        // Extract text safely
        JSONObject firstCandidate = candidates.getJSONObject(0);
        JSONObject content = firstCandidate.optJSONObject("content");
        if (content == null || !content.has("parts")) {
            return "[Error]: No content parts found in candidate.";
        }

        JSONArray parts = content.getJSONArray("parts");
        if (parts.isEmpty()) {
            return "[Error]: Content parts array is empty.";
        }

        String answer = parts.getJSONObject(0).optString("text", "[Error]: No text found.");
        return answer;
    }
}
