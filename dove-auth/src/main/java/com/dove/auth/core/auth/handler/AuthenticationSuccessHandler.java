package com.dove.auth.core.auth.handler;

import com.dove.auth.core.auth.token.TokenService;
import com.dove.auth.core.security.SecurityEventPublisher;
import com.dove.auth.core.security.event.LoginSuccessEvent;
import com.dove.common.core.model.LoginUser;
import com.dove.common.core.model.Result;
import com.dove.common.core.utils.JsonUtils;
import com.dove.common.core.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证成功处理器
 */
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final SecurityEventPublisher eventPublisher;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        
        // 生成token
        String token = tokenService.createToken(loginUser);
        String refreshToken = tokenService.createRefreshToken(loginUser);
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("refreshToken", refreshToken);
        result.put("user", loginUser);
        
        // 发布登录成功事件
        eventPublisher.publishEvent(new LoginSuccessEvent(loginUser));
        
        // 记录登录IP和时间
        loginUser.setLoginIp(ServletUtils.getClientIP(request));
        loginUser.setLoginTime(System.currentTimeMillis());
        
        // 返回token
        ServletUtils.renderString(response, JsonUtils.toJson(Result.success(result)));
    }
} 