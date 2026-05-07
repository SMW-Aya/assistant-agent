package com.srtp.agent.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DocumentCreateRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String source;

    @NotBlank
    private String content;
}
