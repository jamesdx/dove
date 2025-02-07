package com.dove.auth.api.dto.response;

import lombok.Data;

/**
 * 登录响应DTO
 */
@Data
public class LoginResponse {

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 令牌类型
     */
    private String tokenType = "Bearer";

    /**
     * 过期时间(秒)
     */
    private Long expiresIn;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 权限列表
     */
    private String[] permissions;

    /**
     * 角色列表
     */
    private String[] roles;

    /**
     * 是否需要双因素认证
     */
    private Boolean requireTwoFactor;

    /**
     * 是否是新设备登录
     */
    private Boolean newDevice;

    /**
     * 上次登录时间
     */
    private String lastLoginTime;

    /**
     * 上次登录IP
     */
    private String lastLoginIp;
} 