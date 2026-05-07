package com.srtp.agent.service;

import com.srtp.agent.dto.ChatResponse;

public interface AgentOrchestrationService {
    ChatResponse ask(String sessionId, String question);
}
