package com.dove.auth.core.security.event;

import com.dove.common.core.model.LoginUser;
import lombok.Getter;

/**
 * 访问拒绝事件
 */
@Getter
public class AccessDeniedEvent extends SecurityEvent {
    
    private final LoginUser loginUser;
    private final String requestUri;

    public AccessDeniedEvent(LoginUser loginUser, String requestUri) {
        super(loginUser);
        this.loginUser = loginUser;
        this.requestUri = requestUri;
    }
} 