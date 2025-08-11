package com.aniket.AiLegalAssistantApplication.model;


import jakarta.persistence.*;

import java.util.Arrays;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private  String fileName;


    private  String contentType;

    @Lob   // Large Object in JPA      (BLOB= binar , CLOB= Character)
    @Column(columnDefinition = "LONGLOB") // LONGLOB can hold 4gb of binray data

    /*
    @Lob only tells JPA “store as large object”, but it does not guarantee the exact SQL type.
    Without the columnDefinition, Hibernate might choose a smaller type like BLOB (max 64 KB) or MEDIUMBLOB (max 16 MB).
     */

    private byte[] data; // for storing the file content

    public Document() {
    }

    public Document( String fileName, String contentType, byte[] data) {

        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
