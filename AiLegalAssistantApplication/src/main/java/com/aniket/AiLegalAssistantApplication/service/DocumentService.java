package com.aniket.AiLegalAssistantApplication.service;

import com.aniket.AiLegalAssistantApplication.Repositories.DocumentRepository;
import com.aniket.AiLegalAssistantApplication.model.Document;
import com.aniket.AiLegalAssistantApplication.util.FileTextExtractor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;


@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final ChunkService chunkService;
    public DocumentService(DocumentRepository documentRepository, ChunkService chunkService) {
        this.documentRepository = documentRepository;
        this.chunkService = chunkService;
    }

    public Document saveDocument(MultipartFile file) throws Exception{

        Document doc = new Document(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
        );

        Document savedDoc = documentRepository.save(doc);

        String textData = FileTextExtractor.extractText(file);

        chunkService.createChunks(savedDoc.getId(), textData);

        return savedDoc;
    }

    public Optional<Document> getDocument(Long id){
        //  an Optional is a container object that may or may not contain a
        //  non-null value. It is part of java.util (introduced in Java 8)
        //  and is designed to reduce NullPointerException
        // Without Optional
        //
        //The compiler doesn’t warn you if the method can return null.
        //
        //You can write code that compiles fine but blows up at runtime with
        // a NullPointerException if the value is missing.

        //With Optional
        //
        //The method cannot return null — it either has a value or is explicitly empty.
        //
        //The compiler forces you to handle the “no value” case before using it,
        // so you won’t accidentally call a method on null.
        //
        //
        return documentRepository.findById(id);
    }

    public List<Document> getAllDocument(){
        return documentRepository.findAll();
    }

    public void deleteDocument(Long id){
        documentRepository.deleteById(id);
    }


}
