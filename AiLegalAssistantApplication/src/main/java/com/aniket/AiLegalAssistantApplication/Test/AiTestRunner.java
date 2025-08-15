package com.aniket.AiLegalAssistantApplication.Test;

import com.aniket.AiLegalAssistantApplication.Repositories.ChunkRepository;
import com.aniket.AiLegalAssistantApplication.model.Chunk;
import com.aniket.AiLegalAssistantApplication.service.AiAnswerService;
import com.aniket.AiLegalAssistantApplication.service.SearchService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AiTestRunner implements CommandLineRunner {

    private final ChunkRepository chunkRepository;
    private final SearchService searchService;
    private final AiAnswerService aiAnswerService;

    public AiTestRunner(ChunkRepository chunkRepository, SearchService searchService, AiAnswerService aiAnswerService) {
        this.chunkRepository = chunkRepository;
        this.searchService = searchService;
        this.aiAnswerService = aiAnswerService;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1️⃣ Create dummy chunks
        Chunk chunk1 = new Chunk();
        chunk1.setContent("The penalty clause states that any breach results in a $5000 fine.");
        chunkRepository.save(chunk1);
        searchService.indexChunk(chunk1);

        Chunk chunk2 = new Chunk();
        chunk2.setContent("Termination clause: Either party may terminate the contract with 30 days notice.");
        chunkRepository.save(chunk2);
        searchService.indexChunk(chunk2);

        Chunk chunk3 = new Chunk();
        chunk3.setContent("Governing law: This agreement is governed by the laws of California.");
        chunkRepository.save(chunk3);
        searchService.indexChunk(chunk3);

        System.out.println("✅ Dummy chunks indexed in memory.");

        // 2️⃣ Call AI answer service
        String query = "What is the penalty clause?";
        try {
            String answer = aiAnswerService.getAnswer(query);
            System.out.println("Query: " + query);
            System.out.println("AI Answer: " + answer);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}