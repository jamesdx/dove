package com.dove.auth.core.security;

import com.dove.auth.core.security.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 安全事件监听器
 */
@Slf4j
@Component
public class SecurityEventListener {

    /**
     * 处理登录成功事件
     */
    @Async
    @EventListener
    public void handleLoginSuccess(LoginSuccessEvent event) {
        log.info("用户登录成功: {}", event.getLoginUser().getUsername());
    }

    /**
     * 处理登录失败事件
     */
    @Async
    @EventListener
    public void handleLoginFailure(LoginFailureEvent event) {
        log.warn("用户登录失败: {}, 原因: {}", event.getUsername(), event.getReason());
    }

    /**
     * 处理登出成功事件
     */
    @Async
    @EventListener
    public void handleLogoutSuccess(LogoutSuccessEvent event) {
        log.info("用户登出成功: {}", event.getLoginUser().getUsername());
    }

    /**
     * 处理访问拒绝事件
     */
    @Async
    @EventListener
    public void handleAccessDenied(AccessDeniedEvent event) {
        log.warn("访问被拒绝: 用户={}, URI={}", event.getLoginUser().getUsername(), event.getRequestUri());
    }

    /**
     * 处理Token过期事件
     */
    @Async
    @EventListener
    public void handleTokenExpired(TokenExpiredEvent event) {
        log.warn("Token已过期: {}", event.getLoginUser().getUsername());
    }

    /**
     * 处理验证码错误事件
     */
    @Async
    @EventListener
    public void handleCaptchaError(CaptchaErrorEvent event) {
        log.warn("验证码错误: ID={}, 原因={}", event.getCaptchaId(), event.getReason());
    }
} 