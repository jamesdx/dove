package com.helix.dove.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.helix.dove.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Role Entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class Role extends BaseEntity {

    /**
     * Role Name
     */
    @TableField("name")
    private String name;

    /**
     * Role Code
     */
    @TableField("code")
    private String code;

    /**
     * Role Description
     */
    @TableField("description")
    private String description;

    /**
     * Status (0: Disabled, 1: Enabled)
     */
    @TableField("status")
    private Integer status;

    /**
     * Sort Order
     */
    @TableField("sort")
    private Integer sort;

    /**
     * Tenant ID
     */
    @TableField("tenant_id")
    private String tenantId;
} 