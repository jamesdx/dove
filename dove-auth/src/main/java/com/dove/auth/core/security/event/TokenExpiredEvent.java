package com.dove.auth.core.security.event;

import com.dove.common.core.model.LoginUser;
import lombok.Getter;

/**
 * Token过期事件
 */
@Getter
public class TokenExpiredEvent extends SecurityEvent {
    
    private final LoginUser loginUser;

    public TokenExpiredEvent(LoginUser loginUser) {
        super(loginUser);
        this.loginUser = loginUser;
    }
} 