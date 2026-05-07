package com.srtp.agent.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "chat_logs")
public class ChatLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sessionId;

    @Column(nullable = false, length = 2000)
    private String question;

    @Column(nullable = false, length = 8000)
    private String answer;

    @Column(length = 1000)
    private String citations;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
