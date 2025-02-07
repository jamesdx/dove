package com.dove.auth.core.event;

import lombok.Getter;

/**
 * 登录失败事件
 */
@Getter
public class LoginFailureEvent extends SecurityEvent {

    /**
     * 用户名
     */
    private final String username;

    /**
     * 失败原因
     */
    private final String reason;

    public LoginFailureEvent(String username, String reason) {
        super(username, SecurityEventType.LOGIN_FAILURE);
        this.username = username;
        this.reason = reason;
    }
} 