package com.dove.auth.core.event;

import com.dove.common.core.model.LoginUser;
import lombok.Getter;

/**
 * 登录成功事件
 */
@Getter
public class LoginSuccessEvent extends SecurityEvent {

    /**
     * 登录用户信息
     */
    private final LoginUser loginUser;

    public LoginSuccessEvent(LoginUser loginUser) {
        super(loginUser, SecurityEventType.LOGIN_SUCCESS);
        this.loginUser = loginUser;
    }
} 