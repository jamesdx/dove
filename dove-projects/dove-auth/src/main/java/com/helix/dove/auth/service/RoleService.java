package com.helix.dove.auth.service;

import com.helix.dove.auth.entity.Role;
import com.helix.dove.auth.entity.Permission;

import java.util.List;

/**
 * Role Service Interface
 */
public interface RoleService {

    /**
     * Get user roles
     *
     * @param userId user id
     * @param tenantId tenant id
     * @return role list
     */
    List<Role> getUserRoles(Long userId, String tenantId);

    /**
     * Get role permissions
     *
     * @param roleId role id
     * @param tenantId tenant id
     * @return permission list
     */
    List<Permission> getRolePermissions(Long roleId, String tenantId);

    /**
     * Get user permissions
     *
     * @param userId user id
     * @param tenantId tenant id
     * @return permission list
     */
    List<Permission> getUserPermissions(Long userId, String tenantId);

    /**
     * Assign role to user
     *
     * @param userId user id
     * @param roleId role id
     * @param tenantId tenant id
     */
    void assignRole(Long userId, Long roleId, String tenantId);

    /**
     * Remove role from user
     *
     * @param userId user id
     * @param roleId role id
     * @param tenantId tenant id
     */
    void removeRole(Long userId, Long roleId, String tenantId);

    /**
     * Assign permission to role
     *
     * @param roleId role id
     * @param permissionId permission id
     * @param tenantId tenant id
     */
    void assignPermission(Long roleId, Long permissionId, String tenantId);

    /**
     * Remove permission from role
     *
     * @param roleId role id
     * @param permissionId permission id
     * @param tenantId tenant id
     */
    void removePermission(Long roleId, Long permissionId, String tenantId);
} 