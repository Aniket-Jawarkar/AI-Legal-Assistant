package com.aniket.AiLegalAssistantApplication.service;

import com.aniket.AiLegalAssistantApplication.Repositories.DocumentRepository;
import com.aniket.AiLegalAssistantApplication.model.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.util.List;


@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document saveDocument(MultipartFile file) throws Exception{

        Document doc = new Document(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
        );
        return documentRepository.save(doc);
    }

    public  Document getDocument(Long id){

        return documentRepository.getReferenceById(id);
    }

    public List<Document> getAllDocument(){
        return documentRepository.findAll();
    }

    public void deleteDocument(Long id){
        documentRepository.deleteById(id);
    }


}
