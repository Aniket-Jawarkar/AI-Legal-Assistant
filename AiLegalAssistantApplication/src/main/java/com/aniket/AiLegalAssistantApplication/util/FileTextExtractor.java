package com.aniket.AiLegalAssistantApplication.util;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public class FileTextExtractor {

    public static String extractText(MultipartFile file) throws Exception{

        String fileType = file.getContentType();

        if(file.getContentType() == null) fileType = "";

        if(fileType.equals("application/pdf") || file.getOriginalFilename().endsWith(".pdf")){
            return extractPdf(file);
        }else if(fileType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") || file.getOriginalFilename().endsWith(".docx")){
            return extractDocx(file);
        }else{
            return new String(file.getBytes());
        }
    }

    private static String extractPdf(MultipartFile file) throws Exception {
        byte[] bytes = file.getBytes(); // read all bytes
        try (PDDocument document = Loader.loadPDF(bytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private static String extractDocx(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream();
             XWPFDocument doc = new XWPFDocument(is);
             XWPFWordExtractor extractor = new XWPFWordExtractor(doc)) {
            return extractor.getText();
        }
    }
}
