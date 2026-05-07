package com.srtp.agent.service.impl;

import com.srtp.agent.dto.ChatResponse;
import com.srtp.agent.entity.ChatLog;
import com.srtp.agent.repository.ChatLogRepository;
import com.srtp.agent.service.AgentOrchestrationService;
import com.srtp.agent.service.VectorStoreService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class AgentOrchestrationServiceImpl implements AgentOrchestrationService {
    private final VectorStoreService vectorStoreService;
    private final ChatLogRepository chatLogRepository;
    private final StringRedisTemplate redisTemplate;
    private final ChatClient chatClient;
    private final String sessionPrefix;

    public AgentOrchestrationServiceImpl(
            VectorStoreService vectorStoreService,
            ChatLogRepository chatLogRepository,
            StringRedisTemplate redisTemplate,
            ChatClient.Builder chatClientBuilder,
            @Value("${app.chat.session-prefix}") String sessionPrefix
    ) {
        this.vectorStoreService = vectorStoreService;
        this.chatLogRepository = chatLogRepository;
        this.redisTemplate = redisTemplate;
        this.chatClient = chatClientBuilder.build();
        this.sessionPrefix = sessionPrefix;
    }

    @Override
    public ChatResponse ask(String sessionId, String question) {
        List<String> contexts = vectorStoreService.searchRelevantChunks(question, 3);
        String citations = String.join("\n", contexts);

        // Guard: if no usable context, explicitly refuse with guidance.
        if (contexts.isEmpty()) {
            return new ChatResponse("当前资料库中没有足够依据回答该问题，请先上传相关课程资料。", List.of());
        }

        String historyKey = sessionPrefix + sessionId;
        String history = redisTemplate.opsForValue().get(historyKey);
        if (history == null) {
            history = "";
        }

        String prompt = "你是课程助教。只能基于给定课程资料作答，不要编造。\n"
                + "历史对话:\n" + history + "\n"
                + "课程资料片段:\n" + citations + "\n"
                + "学生问题:\n" + question + "\n"
                + "请给出清晰答案，并在末尾写出【参考资料】。";

        String answer = chatClient.prompt(prompt).call().content();
        String newHistory = (history + "\nQ:" + question + "\nA:" + answer).trim();
        redisTemplate.opsForValue().set(historyKey, newHistory, Duration.ofHours(8));

        ChatLog log = new ChatLog();
        log.setSessionId(sessionId);
        log.setQuestion(question);
        log.setAnswer(answer);
        log.setCitations(citations);
        chatLogRepository.save(log);

        return new ChatResponse(answer, contexts);
    }
}
