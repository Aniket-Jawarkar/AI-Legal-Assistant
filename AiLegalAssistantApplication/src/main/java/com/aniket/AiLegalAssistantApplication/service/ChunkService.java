package com.aniket.AiLegalAssistantApplication.service;


import com.aniket.AiLegalAssistantApplication.Repositories.ChunkRepository;
import com.aniket.AiLegalAssistantApplication.model.Chunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChunkService {

    private final ChunkRepository chunkRepository;
    private final EmbeddingService embeddingService;
    private final SearchService searchService; // inject SearchService

    public ChunkService(ChunkRepository chunkRepository,
                        EmbeddingService embeddingService,
                        SearchService searchService) {
        this.chunkRepository = chunkRepository;
        this.embeddingService = embeddingService;
        this.searchService = searchService;
    }

    public void createChunks(Long documentId, String textContent) {
        if (textContent == null || textContent.isEmpty()) {
            return;
        }

        int chunkSize = 500;
        List<Chunk> chunks = new ArrayList<>();

        for (int i = 0; i < textContent.length(); i += chunkSize) {
            String chunkText = textContent.substring(i, Math.min(i + chunkSize, textContent.length()));

            // Generate embedding
            List<Float> embedding = embeddingService.generateEmbedding(chunkText);

            // Store as string (MVP)
            String embeddingString = embedding.toString();

            chunks.add(new Chunk(documentId, chunkText, embeddingString));
        }

        // Save to database
        chunkRepository.saveAll(chunks);

        // Index in memory for semantic search
        for (Chunk chunk : chunks) {
            searchService.indexChunk(chunk);
        }

        System.out.println("✅ Chunks saved and indexed in memory.");
    }
}


