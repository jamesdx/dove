package com.dove.auth.core.session.model;

import com.dove.common.core.model.LoginUser;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会话模型
 */
@Data
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会话ID
     */
    private String id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

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
     * 会话状态（active/expired/invalidated）
     */
    private String status;

    /**
     * 创建会话
     */
    public static Session create(LoginUser loginUser, String loginIp, String deviceInfo, int timeoutMinutes) {
        Session session = new Session();
        session.setId(java.util.UUID.randomUUID().toString());
        session.setUserId(loginUser.getUserId());
        session.setUsername(loginUser.getUsername());
        session.setLoginIp(loginIp);
        session.setDeviceInfo(deviceInfo);
        session.setLoginTime(LocalDateTime.now());
        session.setLastAccessTime(LocalDateTime.now());
        session.setExpireTime(LocalDateTime.now().plusMinutes(timeoutMinutes));
        session.setStatus("active");
        return session;
    }

    /**
     * 更新访问时间
     */
    public void touch() {
        this.lastAccessTime = LocalDateTime.now();
    }

    /**
     * 是否已过期
     */
    public boolean isExpired() {
        return "expired".equals(status) || 
               (expireTime != null && LocalDateTime.now().isAfter(expireTime));
    }

    /**
     * 使会话过期
     */
    public void expire() {
        this.status = "expired";
    }

    /**
     * 使会话失效
     */
    public void invalidate() {
        this.status = "invalidated";
    }
} 