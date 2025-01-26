package com.helix.dove.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.helix.dove.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * User Session Entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_session")
public class UserSession extends BaseEntity {

    /**
     * User ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * Token
     */
    @TableField("token")
    private String token;

    /**
     * Refresh Token
     */
    @TableField("refresh_token")
    private String refreshToken;

    /**
     * Token Expiration Time
     */
    @TableField("token_expiration_time")
    private LocalDateTime tokenExpirationTime;

    /**
     * Refresh Token Expiration Time
     */
    @TableField("refresh_token_expiration_time")
    private LocalDateTime refreshTokenExpirationTime;

    /**
     * Device Info
     */
    @TableField("device_info")
    private String deviceInfo;

    /**
     * Login IP
     */
    @TableField("login_ip")
    private String loginIp;

    /**
     * Last Access Time
     */
    @TableField("last_access_time")
    private LocalDateTime lastAccessTime;

    /**
     * Status (0: Invalid, 1: Valid)
     */
    @TableField("status")
    private Integer status;

    /**
     * Tenant ID
     */
    @TableField("tenant_id")
    private String tenantId;
} 