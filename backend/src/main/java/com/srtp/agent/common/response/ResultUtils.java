package com.srtp.agent.common.response;

import com.srtp.agent.common.error.ErrorCode;

public final class ResultUtils {
    private ResultUtils() {
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(), data, ErrorCode.SUCCESS.getMessage());
    }

    public static BaseResponse<Void> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getCode(), null, errorCode.getMessage());
    }

    public static BaseResponse<Void> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }
}
