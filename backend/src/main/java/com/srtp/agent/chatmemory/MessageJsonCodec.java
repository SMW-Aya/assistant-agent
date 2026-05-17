package com.srtp.agent.chatmemory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MessageJsonCodec {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private MessageJsonCodec() {
    }

    public static String encode(List<Message> messages) {
        try {
            List<Map<String, String>> rows = new ArrayList<>();
            for (Message message : messages) {
                Map<String, String> row = new HashMap<>();
                row.put("type", message.getMessageType().name());
                row.put("text", message.getText());
                rows.add(row);
            }
            return MAPPER.writeValueAsString(rows);
        } catch (Exception e) {
            throw new RuntimeException("encode chat messages failed", e);
        }
    }

    public static List<Message> decode(String json) {
        try {
            List<Map<String, String>> rows = MAPPER.readValue(json, new TypeReference<>() {});
            List<Message> messages = new ArrayList<>();
            for (Map<String, String> row : rows) {
                String type = row.getOrDefault("type", "USER");
                String text = row.getOrDefault("text", "");
                switch (type) {
                    case "ASSISTANT" -> messages.add(new AssistantMessage(text));
                    case "SYSTEM" -> messages.add(new SystemMessage(text));
                    default -> messages.add(new UserMessage(text));
                }
            }
            return messages;
        } catch (Exception e) {
            throw new RuntimeException("decode chat messages failed", e);
        }
    }
}
