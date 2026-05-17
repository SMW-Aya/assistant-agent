package com.srtp.agent.chatmemory;

import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public class RedisChatMemoryRepository implements ChatMemoryRepository {

    private static final String PREFIX = "chat:memory:";

    private final StringRedisTemplate redisTemplate;

    public RedisChatMemoryRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<String> findConversationIds() {
        Set<String> keys = redisTemplate.keys(PREFIX + "*");
        if (keys == null || keys.isEmpty()) {
            return Collections.emptyList();
        }
        return keys.stream().map(k -> k.substring(PREFIX.length())).toList();
    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        String key = PREFIX + conversationId;
        String payload = redisTemplate.opsForValue().get(key);
        if (payload == null || payload.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return MessageJsonCodec.decode(payload);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        String key = PREFIX + conversationId;
        String payload = MessageJsonCodec.encode(messages);
        redisTemplate.opsForValue().set(key, payload);
    }

    @Override
    public void deleteByConversationId(String conversationId) {
        redisTemplate.delete(PREFIX + conversationId);
    }
}
