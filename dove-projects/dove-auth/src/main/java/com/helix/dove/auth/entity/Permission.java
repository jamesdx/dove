package com.helix.dove.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.helix.dove.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Permission Entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_permission")
public class Permission extends BaseEntity {

    /**
     * Permission Name
     */
    @TableField("name")
    private String name;

    /**
     * Permission Code
     */
    @TableField("code")
    private String code;

    /**
     * Permission Type (1: Menu, 2: Button, 3: API)
     */
    @TableField("type")
    private Integer type;

    /**
     * Parent ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * Path
     */
    @TableField("path")
    private String path;

    /**
     * Component
     */
    @TableField("component")
    private String component;

    /**
     * Redirect
     */
    @TableField("redirect")
    private String redirect;

    /**
     * Icon
     */
    @TableField("icon")
    private String icon;

    /**
     * Title
     */
    @TableField("title")
    private String title;

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
     * Keep Alive (0: No, 1: Yes)
     */
    @TableField("keep_alive")
    private Integer keepAlive;

    /**
     * Hidden (0: No, 1: Yes)
     */
    @TableField("hidden")
    private Integer hidden;

    /**
     * Tenant ID
     */
    @TableField("tenant_id")
    private String tenantId;
} 