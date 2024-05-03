package com.ai.rag.services.impl;

import com.ai.rag.configuration.ApplicationProperties;
import com.ai.rag.services.ChatService;
import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.azure.openai.AzureOpenAiChatClient;
import org.springframework.ai.azure.openai.AzureOpenAiChatOptions;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService {

    private final VectorStore vectorStore;

    private final ApplicationProperties applicationProperties;

    private String apiKey;

    private String deploymentName;

    private String azureOpenAiChatEndpoint;

    private Float temperature;

    private int maxToken;

    public ChatServiceImpl(VectorStore vectorStore, ApplicationProperties applicationProperties) {
        this.vectorStore = vectorStore;
        this.applicationProperties = applicationProperties;
    }

    @PostConstruct
    private void init() {
        apiKey = applicationProperties.getAzureOpenAiApiKey();
        deploymentName = applicationProperties.getAzureOpenAiDeploymentName();
        temperature = applicationProperties.getModelTemperature();
        maxToken = applicationProperties.getModelMaxTokens();
        azureOpenAiChatEndpoint = applicationProperties.getAzureOpenAiChatEndpoint();
    }

    @Override
    public String askLlm(String query) {
        List<Document> documentList = vectorStore.similaritySearch(query);

        String systemMessageTemplate = """
                Answer the question in JSON format but do not add ```json``` , based solely on the provided CONTEXT.
                If the answer is not found in the context, respond 'I don't know'.
                CONTEXT:
                     {CONTEXT}
                """;

        Message systemMessage = new SystemPromptTemplate(systemMessageTemplate)
                .createMessage(Map.of("CONTEXT", documentList));
        UserMessage userMessage = new UserMessage(query);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        OpenAIClient openAIClient = new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(apiKey))
                .endpoint(azureOpenAiChatEndpoint)
                .buildClient();
        AzureOpenAiChatOptions azureOpenAiChatOptions = AzureOpenAiChatOptions.builder()
                .withDeploymentName(deploymentName)
                .withTemperature(temperature)
                .withMaxTokens(maxToken)
                .build();
        AzureOpenAiChatClient azureOpenAiChatClient = new AzureOpenAiChatClient(openAIClient, azureOpenAiChatOptions);
        ChatResponse response = azureOpenAiChatClient.call(prompt);
        String responseContent = response.getResult().getOutput().getContent();
        return responseContent;

    }
}
