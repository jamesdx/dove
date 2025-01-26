package com.helix.dove.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.helix.dove.auth.entity.Permission;
import com.helix.dove.auth.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Role Mapper Interface
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * Get permissions by IDs
     *
     * @param permissionIds permission IDs
     * @param tenantId tenant ID
     * @return permission list
     */
    List<Permission> getPermissionsByIds(@Param("permissionIds") List<Long> permissionIds, @Param("tenantId") String tenantId);

    /**
     * Get permission by ID
     *
     * @param permissionId permission ID
     * @return permission
     */
    Permission getPermissionById(@Param("permissionId") Long permissionId);
} 