package com.srtp.agent.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatRequest {
    @NotBlank
    private String sessionId;

    @NotBlank
    private String question;
}
