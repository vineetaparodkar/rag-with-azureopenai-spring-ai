package com.ai.rag.services.impl;

import com.ai.rag.services.TextEmbeddingService;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ai.document.Document;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TextEmbeddingServiceImpl implements TextEmbeddingService {

    private final VectorStore vectorStore;

    public TextEmbeddingServiceImpl(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void textEmbeddingPdf(MultipartFile[] pdfFiles) {
        List<Resource> resources = Arrays.stream(pdfFiles)
                .map(file -> {
                    try {
                        return new ByteArrayResource(file.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        processResources(resources.toArray(new Resource[0]));
    }

    private void processResources(Resource[] resources) {
//        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.defaultConfig();
//        String content = "";
//        for (Resource resource : resources) {
//            PagePdfDocumentReader pagePdfDocumentReader = new PagePdfDocumentReader(resource, config);
//            List<Document> documentList = pagePdfDocumentReader.get();
//            content += documentList.stream().map(d -> d.getContent()).collect(Collectors.joining("\n")) + "\n";
//        }
//
//        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
//        List<String> chunks = tokenTextSplitter.split(content, 1000);
//        List<Document> chunksDocs = chunks.stream().map(chunk -> new Document(chunk)).collect(Collectors.toList());
//        vectorStore.accept(chunksDocs);


        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.defaultConfig();
        List<Document> chunksDocs = new ArrayList<>();
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        for (Resource resource : resources) {
            PagePdfDocumentReader pagePdfDocumentReader = new PagePdfDocumentReader(resource, config);
            List<Document> documentList = pagePdfDocumentReader.get();
            for (Document document : documentList) {
                String content = document.getContent();
                List<String> chunks = tokenTextSplitter.split(content, 1000); // Split content into chunks
                Map<String, Object> documentMetadata = document.getMetadata(); // Get page number
                int chunkCounter = 1; // Initialize chunk counter
                for (String chunk : chunks) {
                    documentMetadata.put("chunk",chunkCounter);
                    chunksDocs.add(new Document(chunk, documentMetadata)); // Add chunk with metadata
                    chunkCounter++; // Increment chunk counter
                }
            }
        }
        // Accept all the chunks with their metadata
        vectorStore.accept(chunksDocs);
    }
}
