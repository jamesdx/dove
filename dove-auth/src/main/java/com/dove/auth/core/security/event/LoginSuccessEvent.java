package com.dove.auth.core.security.event;

import com.dove.common.core.model.LoginUser;
import lombok.Getter;

/**
 * 登录成功事件
 */
@Getter
public class LoginSuccessEvent extends SecurityEvent {
    
    private final LoginUser loginUser;

    public LoginSuccessEvent(LoginUser loginUser) {
        super(loginUser);
        this.loginUser = loginUser;
    }
} 