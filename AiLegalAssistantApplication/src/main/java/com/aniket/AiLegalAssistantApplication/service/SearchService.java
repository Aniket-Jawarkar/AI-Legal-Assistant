package com.aniket.AiLegalAssistantApplication.service;

import com.aniket.AiLegalAssistantApplication.Repositories.ChunkRepository;
import com.aniket.AiLegalAssistantApplication.model.Chunk;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final ChunkRepository chunkRepository;
    private final EmbeddingService embeddingService;

    // In-memory store: chunkId -> embedding vector
    private final Map<Long, float[]> chunkEmbeddings = new HashMap<>();

    public SearchService(ChunkRepository chunkRepository, EmbeddingService embeddingService) {
        this.chunkRepository = chunkRepository;
        this.embeddingService = embeddingService;
    }

    // Save chunk embedding to memory (call this after chunk is created)
    public void indexChunk(Chunk chunk) {
        List<Float> vectorList = embeddingService.generateEmbedding(chunk.getContent());
        float[] vector = convertToPrimitive(vectorList);
        chunkEmbeddings.put(chunk.getId(), vector);
    }

    // Search top N relevant chunks
    public List<Chunk> searchRelevantChunks(String query, int topN) {
        List<Float> queryList = embeddingService.generateEmbedding(query);
        float[] queryVector = convertToPrimitive(queryList);

        return chunkEmbeddings.entrySet()
                .stream()
                .map(entry -> {
                    Chunk chunk = chunkRepository.getReferenceById(entry.getKey());
                    float score = cosineSimilarity(queryVector, entry.getValue());
                    return new AbstractMap.SimpleEntry<>(chunk, score);
                })
                .sorted((a, b) -> Float.compare(b.getValue(), a.getValue())) // sort by score desc
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // Cosine similarity between two vectors
    private float cosineSimilarity(float[] vec1, float[] vec2) {
        if (vec1.length != vec2.length) return -1f; // safety check

        float dot = 0.0f;
        float normA = 0.0f;
        float normB = 0.0f;

        for (int i = 0; i < vec1.length; i++) {
            dot += vec1[i] * vec2[i];
            normA += vec1[i] * vec1[i];
            normB += vec2[i] * vec2[i];
        }

        return (float) (dot / (Math.sqrt(normA) * Math.sqrt(normB)));
    }

    // Helper to convert List<Float> → float[]
    private float[] convertToPrimitive(List<Float> list) {
        float[] arr = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }
}