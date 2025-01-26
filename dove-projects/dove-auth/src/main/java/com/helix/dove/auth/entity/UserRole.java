package com.helix.dove.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.helix.dove.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User Role Entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_role")
public class UserRole extends BaseEntity {

    /**
     * User ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * Role ID
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * Tenant ID
     */
    @TableField("tenant_id")
    private String tenantId;
} 