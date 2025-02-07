package com.dove.auth.core.auth.handler;

import com.dove.auth.core.security.SecurityEventPublisher;
import com.dove.auth.core.security.event.AccessDeniedEvent;
import com.dove.common.core.model.LoginUser;
import com.dove.common.core.model.Result;
import com.dove.common.core.utils.JsonUtils;
import com.dove.common.core.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 访问拒绝处理器
 * 处理已认证用户访问无权限资源时的异常
 */
@Component
@RequiredArgsConstructor
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    private final SecurityEventPublisher eventPublisher;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                      AccessDeniedException exception) throws IOException, ServletException {
        // 获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = authentication != null ? (LoginUser) authentication.getPrincipal() : null;
        
        // 发布访问拒绝事件
        if (loginUser != null) {
            String requestUri = request.getRequestURI();
            eventPublisher.publishEvent(new AccessDeniedEvent(loginUser, requestUri));
        }

        // 设置响应状态码
        int code = HttpServletResponse.SC_FORBIDDEN;
        String message = "您没有访问该资源的权限";

        // 根据不同异常返回不同提示信息
        if (exception != null) {
            if (exception instanceof AccessDeniedException) {
                message = String.format("请求访问：%s，权限不足，无法访问系统资源", request.getRequestURI());
            }
        }

        response.setStatus(code);
        ServletUtils.renderString(response, JsonUtils.toJson(Result.error(code, message)));
    }
} 