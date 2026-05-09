package com.srtp.agent.rag;

import com.srtp.agent.entity.CourseDocument;
import com.srtp.agent.repository.CourseDocumentRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CourseDocumentLoader {

    private final ResourcePatternResolver resourcePatternResolver;
    private final CourseDocumentRepository repository;

    public CourseDocumentLoader(ResourcePatternResolver resourcePatternResolver, CourseDocumentRepository repository) {
        this.resourcePatternResolver = resourcePatternResolver;
        this.repository = repository;
    }

    public List<Document> loadAll() {
        List<Document> all = new ArrayList<>();

        for (CourseDocument d : repository.findAll()) {
            all.add(new Document(d.getContent(),
                    java.util.Map.of("title", d.getTitle(), "source", d.getSource(), "docId", String.valueOf(d.getId()))));
        }

        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.md");
            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeCodeBlock(false)
                        .withIncludeBlockquote(false)
                        .withAdditionalMetadata("filename", fileName)
                        .build();
                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
                all.addAll(reader.get());
            }
        } catch (IOException ignored) {
        }
        return all;
    }
}
