package com.dove.auth.core.session.model;

import com.dove.common.core.model.LoginUser;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会话信息
 */
@Data
public class SessionInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 登录用户
     */
    private LoginUser loginUser;

    /**
     * 登录IP
     */
    private String loginIp;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 最后访问时间
     */
    private LocalDateTime lastAccessTime;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 设备信息
     */
    private String deviceInfo;

    /**
     * 是否已过期
     */
    private boolean expired;

    /**
     * 是否已注销
     */
    private boolean loggedOut;

    /**
     * 更新最后访问时间
     */
    public void updateLastAccessTime() {
        this.lastAccessTime = LocalDateTime.now();
    }

    /**
     * 判断是否已过期
     */
    public boolean isExpired() {
        return expired || (expireTime != null && LocalDateTime.now().isAfter(expireTime));
    }
} 