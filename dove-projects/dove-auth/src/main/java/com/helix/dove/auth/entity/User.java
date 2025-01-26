package com.helix.dove.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.helix.dove.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
     * Password
     */
    @TableField("password")
    private String password;

    /**
     * Email
     */
    @TableField("email")
    private String email;

    /**
     * Mobile
     */
    @TableField("mobile")
    private String mobile;

    /**
     * Nickname
     */
    @TableField("nickname")
    private String nickname;

    /**
     * Avatar
     */
    @TableField("avatar")
    private String avatar;

    /**
     * Status (0: Disabled, 1: Enabled)
     */
    @TableField("status")
    private Integer status;

    /**
     * Tenant ID
     */
    @TableField("tenant_id")
    private String tenantId;

    /**
     * Department ID
     */
    @TableField("dept_id")
    private Long deptId;

    /**
     * Last Login Time
     */
    @TableField("last_login_time")
    private java.time.LocalDateTime lastLoginTime;

    /**
     * Last Login IP
     */
    @TableField("last_login_ip")
    private String lastLoginIp;
} 