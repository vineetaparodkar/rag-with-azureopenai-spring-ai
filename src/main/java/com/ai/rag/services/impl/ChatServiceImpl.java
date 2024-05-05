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

        Set<String> collect = documentList.stream().map(Document::getMetadata).map(m -> (String) m.get("file_name")).collect(Collectors.toSet());

        String fileNames = String.join(",", collect);

        String systemMessageTemplate = """
                You are a helpful assistant helping users with their questions.
                Use the information from the CONTEXT section to provide accurate and detailed answers. If unsure or if the answer
                isn't found in either the CONTEXT section, simply state that you don't know the answer.
                Respond with "No relevant information found." if no relevant information were found from the CONTEXT section.
                                
                After answering the question, provide the names of the files that was referred to. Use REFERENCES section below to provide details of the file names.
                                
                CONTEXT:
                {CONTEXT}
                                
                REFERENCES:
                {fileNames}

                """;

        Message systemMessage = new SystemPromptTemplate(systemMessageTemplate)
                .createMessage(Map.of(
                        "CONTEXT", documentList,
                        "fileNames", fileNames
                ));
        UserMessage userMessage = new UserMessage(query);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        OpenAiApi aiApi = new OpenAiApi(apiKey);
        OpenAiChatOptions openAiChatOptions = OpenAiChatOptions.builder()
                .withModel(model)
                .withTemperature(temperature)
                .withMaxTokens(maxToken)
                .build();
        OpenAiChatClient openAiChatClient = new OpenAiChatClient(aiApi, openAiChatOptions);
        ChatResponse response = openAiChatClient.call(prompt);
        String responseContent = response.getResult().getOutput().getContent();
        return responseContent;

    }
}
