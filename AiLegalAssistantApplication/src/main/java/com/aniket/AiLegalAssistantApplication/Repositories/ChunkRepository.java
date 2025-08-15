package com.aniket.AiLegalAssistantApplication.Repositories;

import com.aniket.AiLegalAssistantApplication.model.Chunk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChunkRepository extends JpaRepository<Chunk, Long> {
}
