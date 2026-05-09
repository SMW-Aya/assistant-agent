package com.srtp.agent.common.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String requestId;
    private LocalDateTime requestTime = LocalDateTime.now();
}
