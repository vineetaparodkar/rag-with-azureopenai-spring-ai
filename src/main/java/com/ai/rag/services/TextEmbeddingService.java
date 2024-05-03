package com.ai.rag.services;

import org.springframework.web.multipart.MultipartFile;

public interface TextEmbeddingService {

    void textEmbeddingPdf(MultipartFile[] pdfFiles);
}
