package com.ai.rag.controller;

import com.ai.rag.services.TextEmbeddingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class RagController {

    private final TextEmbeddingService textEmbeddingService;

    public RagController(TextEmbeddingService textEmbeddingService) {
        this.textEmbeddingService = textEmbeddingService;
    }

    @PostMapping("/upload/pdf")
    public ResponseEntity<String> textEmbeddingsPdf(@RequestParam("files") MultipartFile[] pdfFiles) throws IOException {
        textEmbeddingService.textEmbeddingPdf(pdfFiles);
        return ResponseEntity.ok().build();
    }
}