package com.ai.rag.controller;

import com.ai.rag.services.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public class LlmController {

    private final ChatService chatService;

    public LlmController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/llm/ask")
    public Map askLlm(@RequestParam(name = "query") String query) throws JsonProcessingException {
        String response = chatService.askLlm(query);
        return new ObjectMapper().readValue(response, Map.class);
    }
}
