package com.aniket.AiLegalAssistantApplication.model;

import jakarta.persistence.*;

@Entity
@Table(name = "`chunk`")
public class Chunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long documentId;

    @Lob
    @Column(nullable = true, columnDefinition = "LONGTEXT") // prevents default value issue
    private String content;

    @Lob
    @Column(nullable = true, columnDefinition = "LONGTEXT") // prevents default value issue
    private String embedding;

    public Chunk() {
    }

    public Chunk(Long documentId, String content, String embedding) {
        this.documentId = documentId;
        this.content = content;
        this.embedding = embedding;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmbedding() {
        return embedding;
    }

    public void setEmbedding(String embedding) {
        this.embedding = embedding;
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "id=" + id +
                ", documentId=" + documentId +
                ", content='" + content + '\'' +
                '}';
    }
}
