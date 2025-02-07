package com.dove.auth.core.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 安全事件基类
 */
@Getter
public abstract class SecurityEvent extends ApplicationEvent {

    /**
     * 事件时间戳
     */
    private final long timestamp;

    /**
     * 事件类型
     */
    private final SecurityEventType eventType;

    public SecurityEvent(Object source, SecurityEventType eventType) {
        super(source);
        this.timestamp = System.currentTimeMillis();
        this.eventType = eventType;
    }

    /**
     * 安全事件类型枚举
     */
    public enum SecurityEventType {
        /**
         * 登录成功
         */
        LOGIN_SUCCESS,

        /**
         * 登录失败
         */
        LOGIN_FAILURE,

        /**
         * 登出成功
         */
        LOGOUT_SUCCESS,

        /**
         * 密码错误
         */
        PASSWORD_ERROR,

        /**
         * 验证码错误
         */
        CAPTCHA_ERROR,

        /**
         * 账号锁定
         */
        ACCOUNT_LOCKED,

        /**
         * 账号过期
         */
        ACCOUNT_EXPIRED,

        /**
         * 密码过期
         */
        PASSWORD_EXPIRED,

        /**
         * 权限不足
         */
        ACCESS_DENIED,

        /**
         * 会话过期
         */
        SESSION_EXPIRED,

        /**
         * Token过期
         */
        TOKEN_EXPIRED,

        /**
         * Token刷新
         */
        TOKEN_REFRESH,

        /**
         * 异常登录
         */
        ABNORMAL_LOGIN
    }
} 