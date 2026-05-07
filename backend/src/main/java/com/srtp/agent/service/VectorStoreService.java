package com.srtp.agent.service;

import java.util.List;

public interface VectorStoreService {
    void upsert(Long documentId, String title, String content);
    List<String> searchRelevantChunks(String query, int topK);
}
