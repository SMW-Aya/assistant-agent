package com.srtp.agent.service.impl;

import com.srtp.agent.app.CourseAssistantApp;
import com.srtp.agent.dto.ChatResponse;
import com.srtp.agent.entity.ChatLog;
import com.srtp.agent.repository.ChatLogRepository;
import com.srtp.agent.service.AgentOrchestrationService;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentOrchestrationServiceImpl implements AgentOrchestrationService {

    private final CourseAssistantApp courseAssistantApp;
    private final ChatLogRepository chatLogRepository;
    private final VectorStore courseVectorStore;

    public AgentOrchestrationServiceImpl(
            CourseAssistantApp courseAssistantApp,
            ChatLogRepository chatLogRepository,
            VectorStore courseVectorStore
    ) {
        this.courseAssistantApp = courseAssistantApp;
        this.chatLogRepository = chatLogRepository;
        this.courseVectorStore = courseVectorStore;
    }

    @Override
    public ChatResponse ask(String sessionId, String question) {
        List<Document> docs = courseVectorStore.similaritySearch(SearchRequest.builder().query(question).topK(3).build());
        List<String> citations = docs.stream()
                .map(d -> {
                    String title = String.valueOf(d.getMetadata().getOrDefault("title", d.getMetadata().getOrDefault("filename", "unknown")));
                    String snippet = d.getText();
                    if (snippet.length() > 120) {
                        snippet = snippet.substring(0, 120) + "...";
                    }
                    return "[" + title + "] " + snippet;
                })
                .toList();

        if (citations.isEmpty()) {
            return new ChatResponse("当前资料库中没有足够依据回答该问题，请先上传相关课程资料。", List.of());
        }

        String answer = courseAssistantApp.doChatWithRag(question, sessionId);

        ChatLog log = new ChatLog();
        log.setSessionId(sessionId);
        log.setQuestion(question);
        log.setAnswer(answer);
        log.setCitations(String.join("\n", citations));
        chatLogRepository.save(log);

        return new ChatResponse(answer, citations);
    }
}
