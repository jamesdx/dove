package com.dove.auth.core.security.event;

import lombok.Getter;

/**
 * 登录失败事件
 */
@Getter
public class LoginFailureEvent extends SecurityEvent {
    
    private final String username;
    private final String reason;

    public LoginFailureEvent(String username, String reason) {
        super(username);
        this.username = username;
        this.reason = reason;
    }
} 