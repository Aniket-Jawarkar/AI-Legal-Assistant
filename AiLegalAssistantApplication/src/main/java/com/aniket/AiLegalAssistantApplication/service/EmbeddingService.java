package com.aniket.AiLegalAssistantApplication.service;


import org.hibernate.annotations.SecondaryRow;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmbeddingService {
    public List<Float> generateEmbedding(String text) {
        // For MVP: Fake embedding (random numbers) or call real API later
        // This keeps the pipeline working before integrating Gemini/OpenAI
        List<Float> embedding = new ArrayList<>();
        for (int i = 0; i < 10; i++) { // 10-dim fake vector
            embedding.add((float) Math.random());
        }
        return embedding;
    }
}
