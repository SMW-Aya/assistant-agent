package com.srtp.agent.service.impl;

import com.srtp.agent.service.VectorStoreService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryVectorStoreService implements VectorStoreService {
    private final Map<Long, String> docContent = new ConcurrentHashMap<>();
    private final Map<Long, String> docTitle = new ConcurrentHashMap<>();

    @Override
    public void upsert(Long documentId, String title, String content) {
        docTitle.put(documentId, title);
        docContent.put(documentId, content);
    }

    @Override
    public List<String> searchRelevantChunks(String query, int topK) {
        String q = query.toLowerCase();
        return docContent.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> -score(entry.getValue(), q)))
                .limit(topK)
                .map(entry -> "[" + docTitle.get(entry.getKey()) + "] " + truncate(entry.getValue(), 300))
                .toList();
    }

    private int score(String content, String query) {
        int s = 0;
        for (String token : query.split("\\s+")) {
            if (content.toLowerCase().contains(token)) {
                s++;
            }
        }
        return s;
    }

    private String truncate(String text, int maxLen) {
        if (text.length() <= maxLen) {
            return text;
        }
        return text.substring(0, maxLen) + "...";
    }
}
