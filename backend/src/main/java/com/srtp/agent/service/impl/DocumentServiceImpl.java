package com.srtp.agent.service.impl;

import com.srtp.agent.dto.DocumentCreateRequest;
import com.srtp.agent.entity.CourseDocument;
import com.srtp.agent.repository.CourseDocumentRepository;
import com.srtp.agent.service.DocumentService;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final CourseDocumentRepository repository;
    private final VectorStore courseVectorStore;

    public DocumentServiceImpl(CourseDocumentRepository repository, VectorStore courseVectorStore) {
        this.repository = repository;
        this.courseVectorStore = courseVectorStore;
    }

    @Override
    public CourseDocument create(DocumentCreateRequest request) {
        CourseDocument doc = new CourseDocument();
        doc.setTitle(request.getTitle());
        doc.setSource(request.getSource());
        doc.setContent(request.getContent());
        CourseDocument saved = repository.save(doc);

        courseVectorStore.add(List.of(new Document(saved.getContent(), Map.of(
                "title", saved.getTitle(),
                "source", saved.getSource(),
                "docId", String.valueOf(saved.getId())
        ))));
        return saved;
    }

    @Override
    public List<CourseDocument> list() {
        return repository.findAll();
    }
}
