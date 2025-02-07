package com.dove.auth.core.security;

import com.dove.auth.core.event.SecurityEvent;
import com.dove.common.core.model.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 安全告警服务
 */
@Service
@RequiredArgsConstructor
public class SecurityAlertService {

    /**
     * 处理登录成功事件
     */
    @Async
    @EventListener(condition = "#event.eventType == T(com.dove.auth.core.event.SecurityEvent.SecurityEventType).LOGIN_SUCCESS")
    public void handleLoginSuccess(SecurityEvent event) {
        LoginUser loginUser = (LoginUser) event.getSource();
        // TODO: 记录登录成功日志
        System.out.println("Login success: " + loginUser.getUsername());
    }

    /**
     * 处理登录失败事件
     */
    @Async
    @EventListener(condition = "#event.eventType == T(com.dove.auth.core.event.SecurityEvent.SecurityEventType).LOGIN_FAILURE")
    public void handleLoginFailure(SecurityEvent event) {
        String username = (String) event.getSource();
        // TODO: 记录登录失败日志，触发告警
        System.out.println("Login failure: " + username);
    }

    /**
     * 处理账号锁定事件
     */
    @Async
    @EventListener(condition = "#event.eventType == T(com.dove.auth.core.event.SecurityEvent.SecurityEventType).ACCOUNT_LOCKED")
    public void handleAccountLocked(SecurityEvent event) {
        String username = (String) event.getSource();
        // TODO: 记录账号锁定日志，发送告警通知
        System.out.println("Account locked: " + username);
    }

    /**
     * 处理异常登录事件
     */
    @Async
    @EventListener(condition = "#event.eventType == T(com.dove.auth.core.event.SecurityEvent.SecurityEventType).ABNORMAL_LOGIN")
    public void handleAbnormalLogin(SecurityEvent event) {
        LoginUser loginUser = (LoginUser) event.getSource();
        // TODO: 记录异常登录日志，发送告警通知
        System.out.println("Abnormal login: " + loginUser.getUsername());
    }

    /**
     * 处理Token过期事件
     */
    @Async
    @EventListener(condition = "#event.eventType == T(com.dove.auth.core.event.SecurityEvent.SecurityEventType).TOKEN_EXPIRED")
    public void handleTokenExpired(SecurityEvent event) {
        LoginUser loginUser = (LoginUser) event.getSource();
        // TODO: 记录Token过期日志
        System.out.println("Token expired: " + loginUser.getUsername());
    }

    /**
     * 处理权限不足事件
     */
    @Async
    @EventListener(condition = "#event.eventType == T(com.dove.auth.core.event.SecurityEvent.SecurityEventType).ACCESS_DENIED")
    public void handleAccessDenied(SecurityEvent event) {
        LoginUser loginUser = (LoginUser) event.getSource();
        // TODO: 记录权限不足日志，触发告警
        System.out.println("Access denied: " + loginUser.getUsername());
    }

    /**
     * 处理验证码错误事件
     */
    @Async
    @EventListener(condition = "#event.eventType == T(com.dove.auth.core.event.SecurityEvent.SecurityEventType).CAPTCHA_ERROR")
    public void handleCaptchaError(SecurityEvent event) {
        String username = (String) event.getSource();
        // TODO: 记录验证码错误日志
        System.out.println("Captcha error: " + username);
    }
} 