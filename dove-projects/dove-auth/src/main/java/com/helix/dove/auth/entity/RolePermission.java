package com.helix.dove.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.helix.dove.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Role Permission Entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_permission")
public class RolePermission extends BaseEntity {

    /**
     * Role ID
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * Permission ID
     */
    @TableField("permission_id")
    private Long permissionId;

    /**
     * Tenant ID
     */
    @TableField("tenant_id")
    private String tenantId;
} 