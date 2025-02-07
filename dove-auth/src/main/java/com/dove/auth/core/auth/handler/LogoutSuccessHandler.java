package com.dove.auth.core.auth.handler;

import com.dove.auth.core.auth.token.TokenService;
import com.dove.auth.core.security.SecurityEventPublisher;
import com.dove.auth.core.security.event.LogoutSuccessEvent;
import com.dove.common.core.model.LoginUser;
import com.dove.common.core.model.Result;
import com.dove.common.core.utils.JsonUtils;
import com.dove.common.core.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 登出成功处理器
 */
@Component
@RequiredArgsConstructor
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private final TokenService tokenService;
    private final SecurityEventPublisher eventPublisher;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                               Authentication authentication) throws IOException, ServletException {
        // 获取当前登录用户
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser != null) {
            // 清除用户token
            tokenService.removeToken(loginUser);
            
            // 发布登出成功事件
            eventPublisher.publishEvent(new LogoutSuccessEvent(loginUser));
        }
        
        // 返回成功消息
        ServletUtils.renderString(response, JsonUtils.toJson(Result.success("登出成功")));
    }
} 