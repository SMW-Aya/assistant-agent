package com.srtp.agent.service;

import com.srtp.agent.dto.DocumentCreateRequest;
import com.srtp.agent.entity.CourseDocument;

import java.util.List;

public interface DocumentService {
    CourseDocument create(DocumentCreateRequest request);
    List<CourseDocument> list();
}
