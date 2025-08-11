package com.aniket.AiLegalAssistantApplication.Repositories;

import com.aniket.AiLegalAssistantApplication.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository< Document,Long > {
}
