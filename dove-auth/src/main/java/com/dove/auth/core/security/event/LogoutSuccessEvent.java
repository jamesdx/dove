package com.dove.auth.core.security.event;

import com.dove.common.core.model.LoginUser;
import lombok.Getter;

/**
 * 登出成功事件
 */
@Getter
public class LogoutSuccessEvent extends SecurityEvent {
    
    private final LoginUser loginUser;

    public LogoutSuccessEvent(LoginUser loginUser) {
        super(loginUser);
        this.loginUser = loginUser;
    }
} 