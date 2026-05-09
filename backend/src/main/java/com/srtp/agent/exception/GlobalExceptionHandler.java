package com.srtp.agent.exception;

import com.srtp.agent.common.error.ErrorCode;
import com.srtp.agent.common.response.BaseResponse;
import com.srtp.agent.common.response.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<Void> businessExceptionHandler(BusinessException e) {
        log.warn("BusinessException code={}, message={}", e.getCode(), e.getMessage());
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class, IllegalArgumentException.class})
    public BaseResponse<Void> paramsExceptionHandler(Exception e) {
        log.warn("ParamsException message={}", e.getMessage());
        return ResultUtils.error(ErrorCode.PARAMS_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<Void> runtimeExceptionHandler(Exception e) {
        log.error("SystemException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }
}
