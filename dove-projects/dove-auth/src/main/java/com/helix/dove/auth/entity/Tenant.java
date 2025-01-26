package com.helix.dove.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.helix.dove.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Tenant Entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_tenant")
public class Tenant extends BaseEntity {

    /**
     * Tenant Name
     */
    @TableField("name")
    private String name;

    /**
     * Tenant Code
     */
    @TableField("code")
    private String code;

    /**
     * Status (0: Disabled, 1: Enabled)
     */
    @TableField("status")
    private Integer status;

    /**
     * Contact Name
     */
    @TableField("contact_name")
    private String contactName;

    /**
     * Contact Email
     */
    @TableField("contact_email")
    private String contactEmail;

    /**
     * Contact Phone
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * Address
     */
    @TableField("address")
    private String address;

    /**
     * License Type
     */
    @TableField("license_type")
    private String licenseType;

    /**
     * License Expiration Time
     */
    @TableField("license_expiration_time")
    private java.time.LocalDateTime licenseExpirationTime;

    /**
     * Max User Count
     */
    @TableField("max_user_count")
    private Integer maxUserCount;

    /**
     * Domain
     */
    @TableField("domain")
    private String domain;

    /**
     * Theme
     */
    @TableField("theme")
    private String theme;

    /**
     * Logo
     */
    @TableField("logo")
    private String logo;

    /**
     * Description
     */
    @TableField("description")
    private String description;
} 