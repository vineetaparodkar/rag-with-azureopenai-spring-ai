package com.ai.rag.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApplicationProperties {

    @Value("${web.config.allowed-origin-urls}")
    private String[] allowedOriginURLs;

    @Value("${web.config.max-age}")
    private Long maxAge;

    @Value("${spring.ai.vectorstore.qdrant.host}")
    private String vectorStoreHostname;

    @Value("${spring.ai.vectorstore.qdrant.port}")
    private int vectorStorePort;

    @Value("${spring.ai.vectorstore.qdrant.collection-name}")
    private String vectorStoreCollectionName;

    @Value("${spring.ai.vectorstore.qdrant.api-key}")
    private String vectorStoreApiKey;

    @Value("${spring.ai.azure.openai.chat.options.deployment-name}")
    private String azureOpenAiDeploymentName;

    @Value("${spring.ai.azure.openai.chat.options.temperature}")
    private Float modelTemperature;

    @Value("${spring.ai.azure.openai.chat.options.maxTokens}")
    private int modelMaxTokens;

    @Value("${spring.ai.azure.openai.chat.options.topP}")
    private String modelTopP;

    @Value("${spring.ai.azure.openai.chat.options.frequencyPenalty}")
    private String modelFrequencyPenalty;

    @Value("${spring.ai.azure.openai.chat.options.api-key}")
    private String azureOpenAiApiKey;

    @Value("${spring.ai.azure.openai.endpoint}")
    private String azureOpenAiChatEndpoint;

    public Long getMaxAge() {
        return maxAge;
    }

    public String[] getAllowedOriginURLs() {
        return allowedOriginURLs;
    }

    public String getVectorStoreHostname() {
        return vectorStoreHostname;
    }

    public int getVectorStorePort() {
        return vectorStorePort;
    }

    public String getVectorStoreCollectionName() {
        return vectorStoreCollectionName;
    }

    public String getVectorStoreApiKey() {
        return vectorStoreApiKey;
    }

    public String getAzureOpenAiApiKey() {
        return azureOpenAiApiKey;
    }

    public String getAzureOpenAiDeploymentName() {
        return azureOpenAiDeploymentName;
    }

    public Float getModelTemperature() {
        return modelTemperature;
    }

    public int getModelMaxTokens() {
        return modelMaxTokens;
    }

    public String getModelTopP() {
        return modelTopP;
    }

    public String getModelFrequencyPenalty() {
        return modelFrequencyPenalty;
    }

    public String getAzureOpenAiChatEndpoint() {
        return azureOpenAiChatEndpoint;
    }
}
