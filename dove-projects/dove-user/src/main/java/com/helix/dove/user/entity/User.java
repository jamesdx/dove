package com.helix.dove.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.helix.dove.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * User Entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends BaseEntity {

    /**
     * Username
     */
    @TableField("username")
    private String username;

    /**
     * Email
     */
    @TableField("email")
    private String email;

    /**
     * Password hash
     */
    @TableField("password_hash")
    private String passwordHash;

    /**
     * Status: 1-Normal, 2-Locked, 3-Disabled
     */
    @TableField("status")
    private Integer status;

    /**
     * Account type: 1-Personal, 2-Enterprise
     */
    @TableField("account_type")
    private Integer accountType;

    /**
     * Last login time
     */
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * Last login IP
     */
    @TableField("last_login_ip")
    private String lastLoginIp;

    /**
     * Failed login attempts
     */
    @TableField("failed_attempts")
    private Integer failedAttempts;

    /**
     * Account locked until time
     */
    @TableField("locked_until")
    private LocalDateTime lockedUntil;

    /**
     * Locale preference
     */
    @TableField("locale")
    private String locale;

    /**
     * Timezone
     */
    @TableField("timezone")
    private String timezone;

    /**
     * Date format
     */
    @TableField("date_format")
    private String dateFormat;

    /**
     * Time format
     */
    @TableField("time_format")
    private String timeFormat;

    /**
     * Currency
     */
    @TableField("currency")
    private String currency;

    /**
     * Region
     */
    @TableField("region")
    private String region;

    /**
     * Tenant ID
     */
    @TableField("tenant_id")
    private String tenantId;
} 