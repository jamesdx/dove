package com.dove.auth.core.auth.handler;

import com.dove.common.core.model.Result;
import com.dove.common.core.utils.JsonUtils;
import com.dove.common.core.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 认证入口点处理器
 * 处理匿名用户访问需要认证的资源时的异常
 */
@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException exception) throws IOException, ServletException {
        int code = HttpServletResponse.SC_UNAUTHORIZED;
        String message = "访问此资源需要完全身份验证";
        
        // 根据不同异常返回不同提示信息
        if (exception != null) {
            message = exception.getMessage();
            if ("Bad credentials".equals(message)) {
                message = "用户名或密码错误";
            } else if ("Full authentication is required to access this resource".equals(message)) {
                message = "访问此资源需要完全身份验证";
            }
        }
        
        response.setStatus(code);
        ServletUtils.renderString(response, JsonUtils.toJson(Result.error(code, message)));
    }
} 