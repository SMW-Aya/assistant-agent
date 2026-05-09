package com.srtp.agent.common.error;

public enum ErrorCode {
    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    UNAUTHORIZED(40100, "未登录或鉴权失败"),
    FORBIDDEN(40300, "无访问权限"),
    NOT_FOUND(40400, "资源不存在"),
    SYSTEM_ERROR(50000, "系统内部错误");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
