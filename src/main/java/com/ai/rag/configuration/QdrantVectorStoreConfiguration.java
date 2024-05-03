package com.ai.rag.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.autoconfigure.vectorstore.qdrant.QdrantVectorStoreProperties;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;


@AutoConfiguration
@ConditionalOnClass({ QdrantVectorStore.class, EmbeddingClient.class })
public class QdrantVectorStoreConfiguration {

    private final ApplicationProperties applicationProperties;

    private String hostname;

    private int port;

    private String collectionName;

    private String apiKey;

    public QdrantVectorStoreConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @PostConstruct
    private void init() {
        hostname = applicationProperties.getVectorStoreHostname();
        port = applicationProperties.getVectorStorePort();
        collectionName = applicationProperties.getVectorStoreCollectionName();
        apiKey = applicationProperties.getVectorStoreApiKey();

    }

    @Bean
    @ConditionalOnMissingBean
    public VectorStore vectorStore(EmbeddingClient embeddingClient, QdrantVectorStoreProperties properties) {

        QdrantVectorStore.QdrantVectorStoreConfig config = QdrantVectorStore.QdrantVectorStoreConfig.builder()
                .withCollectionName(properties.getCollectionName())
                .withHost(hostname)
                .withPort(port)
                .withCollectionName(collectionName)
                .withApiKey(apiKey)
                .build();

        return new QdrantVectorStore(config, embeddingClient);
    }
}
