package com.srtp.agent.controller;

import com.srtp.agent.dto.ChatRequest;
import com.srtp.agent.dto.ChatResponse;
import com.srtp.agent.service.AgentOrchestrationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatController {
    private final AgentOrchestrationService orchestrationService;

    public ChatController(AgentOrchestrationService orchestrationService) {
        this.orchestrationService = orchestrationService;
    }

    @PostMapping("/ask")
    public ChatResponse ask(@Valid @RequestBody ChatRequest request) {
        return orchestrationService.ask(request.getSessionId(), request.getQuestion());
    }
}
