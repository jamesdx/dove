package com.dove.auth.core.event;

import com.dove.common.core.model.LoginUser;
import lombok.Getter;

/**
 * 登出成功事件
 */
@Getter
public class LogoutSuccessEvent extends SecurityEvent {

    /**
     * 登录用户信息
     */
    private final LoginUser loginUser;

    public LogoutSuccessEvent(LoginUser loginUser) {
        super(loginUser, SecurityEventType.LOGOUT_SUCCESS);
        this.loginUser = loginUser;
    }
} 