package com.srtp.agent.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
public class BannedWordAdvisor implements CallAdvisor, StreamAdvisor {

    private static final List<String> BANNED_WORDS = List.of("炸药", "爆破", "枪支", "毒品");

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        String userText = extractUserText(request);
        validate(userText);
        return chain.nextCall(request);
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain chain) {
        String userText = extractUserText(request);
        validate(userText);
        return chain.nextStream(request);
    }

    @Override
    public String getName() {
        return "BannedWordAdvisor";
    }

    @Override
    public int getOrder() {
        return -100;
    }

    private void validate(String text) {
        if (text == null || text.isBlank()) {
            return;
        }
        for (String bannedWord : BANNED_WORDS) {
            if (text.contains(bannedWord)) {
                throw new IllegalArgumentException("请求包含违禁词，已拒绝处理");
            }
        }
    }

    private String extractUserText(ChatClientRequest request) {
        if (request == null || request.prompt() == null || request.prompt().getInstructions() == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Message message : request.prompt().getInstructions()) {
            if (message instanceof UserMessage userMessage && userMessage.getText() != null) {
                sb.append(userMessage.getText()).append('\n');
            }
        }
        return sb.toString();
    }
}
