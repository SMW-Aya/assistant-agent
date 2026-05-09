package com.srtp.agent.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
@Component
public class RequestLogInterceptor implements HandlerInterceptor {

    private static final String TRACE_ID = "traceId";
    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = UUID.randomUUID().toString().replace("-", "");
        long startTime = System.currentTimeMillis();

        request.setAttribute(TRACE_ID, traceId);
        request.setAttribute(START_TIME, startTime);
        response.setHeader("X-Trace-Id", traceId);

        log.info("Request start traceId={}, method={}, uri={}, query={}",
                traceId,
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Object traceIdObj = request.getAttribute(TRACE_ID);
        Object startTimeObj = request.getAttribute(START_TIME);
        if (traceIdObj == null || startTimeObj == null) {
            return;
        }
        String traceId = String.valueOf(traceIdObj);
        long startTime = (long) startTimeObj;
        long cost = System.currentTimeMillis() - startTime;

        if (ex == null) {
            log.info("Request end traceId={}, status={}, cost={}ms", traceId, response.getStatus(), cost);
        } else {
            log.error("Request end traceId={}, status={}, cost={}ms, error={}",
                    traceId, response.getStatus(), cost, ex.getMessage(), ex);
        }
    }
}
