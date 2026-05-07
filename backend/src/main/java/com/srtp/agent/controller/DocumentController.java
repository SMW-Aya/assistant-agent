package com.srtp.agent.controller;

import com.srtp.agent.dto.DocumentCreateRequest;
import com.srtp.agent.entity.CourseDocument;
import com.srtp.agent.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public CourseDocument create(@Valid @RequestBody DocumentCreateRequest request) {
        return documentService.create(request);
    }

    @GetMapping
    public List<CourseDocument> list() {
        return documentService.list();
    }
}
