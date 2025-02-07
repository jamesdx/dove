package com.dove.auth.core.auth.handler;

import com.dove.auth.core.security.SecurityEventPublisher;
import com.dove.auth.core.security.event.LoginFailureEvent;
import com.dove.common.core.model.Result;
import com.dove.common.core.utils.JsonUtils;
import com.dove.common.core.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 认证失败处理器
 */
@Component
@RequiredArgsConstructor
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final SecurityEventPublisher eventPublisher;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        String message = translateException(exception);
        
        // 发布登录失败事件
        eventPublisher.publishEvent(new LoginFailureEvent(username, message));
        
        // 返回错误信息
        ServletUtils.renderString(response, JsonUtils.toJson(Result.error(message)));
    }
    
    /**
     * 转换认证异常
     */
    private String translateException(AuthenticationException exception) {
        if (exception instanceof BadCredentialsException) {
            return "用户名或密码错误";
        } else if (exception instanceof DisabledException) {
            return "账户已被禁用";
        } else if (exception instanceof LockedException) {
            return "账户已被锁定";
        } else if (exception instanceof AccountExpiredException) {
            return "账户已过期";
        } else if (exception instanceof CredentialsExpiredException) {
            return "密码已过期";
        } else {
            return "认证失败";
        }
    }
} 