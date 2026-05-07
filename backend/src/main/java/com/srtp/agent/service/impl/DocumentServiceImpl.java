package com.srtp.agent.service.impl;

import com.srtp.agent.dto.DocumentCreateRequest;
import com.srtp.agent.entity.CourseDocument;
import com.srtp.agent.repository.CourseDocumentRepository;
import com.srtp.agent.service.DocumentService;
import com.srtp.agent.service.VectorStoreService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final CourseDocumentRepository repository;
    private final VectorStoreService vectorStoreService;

    public DocumentServiceImpl(CourseDocumentRepository repository, VectorStoreService vectorStoreService) {
        this.repository = repository;
        this.vectorStoreService = vectorStoreService;
    }

    @Override
    public CourseDocument create(DocumentCreateRequest request) {
        CourseDocument doc = new CourseDocument();
        doc.setTitle(request.getTitle());
        doc.setSource(request.getSource());
        doc.setContent(request.getContent());
        CourseDocument saved = repository.save(doc);
        vectorStoreService.upsert(saved.getId(), saved.getTitle(), saved.getContent());
        return saved;
    }

    @Override
    public List<CourseDocument> list() {
        return repository.findAll();
    }
}
