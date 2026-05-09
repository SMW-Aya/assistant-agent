package com.srtp.agent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CourseVectorStoreConfig {

    @Resource
    private CourseDocumentLoader documentLoader;

    @Bean
    VectorStore courseVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        List<Document> documents = documentLoader.loadAll();
        if (!documents.isEmpty()) {
            vectorStore.add(documents);
        }
        return vectorStore;
    }
}
