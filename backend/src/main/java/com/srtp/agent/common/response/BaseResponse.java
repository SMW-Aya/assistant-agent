package com.srtp.agent.common.response;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int code;
    private T data;
    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
}
