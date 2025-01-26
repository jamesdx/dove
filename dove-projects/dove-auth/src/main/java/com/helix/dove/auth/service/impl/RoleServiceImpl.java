package com.helix.dove.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helix.dove.auth.entity.*;
import com.helix.dove.auth.mapper.RoleMapper;
import com.helix.dove.auth.mapper.RolePermissionMapper;
import com.helix.dove.auth.mapper.UserRoleMapper;
import com.helix.dove.auth.service.RoleService;
import com.helix.dove.common.api.ResultCode;
import com.helix.dove.common.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Role Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final UserRoleMapper userRoleMapper;
    private final RolePermissionMapper rolePermissionMapper;

    @Override
    public List<Role> getUserRoles(Long userId, String tenantId) {
        // Get user role IDs
        List<Long> roleIds = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId)
                .eq(UserRole::getTenantId, tenantId))
                .stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());

        // Get roles
        if (!roleIds.isEmpty()) {
            return list(new LambdaQueryWrapper<Role>()
                    .in(Role::getId, roleIds)
                    .eq(Role::getTenantId, tenantId)
                    .eq(Role::getStatus, 1));
        }
        return List.of();
    }

    @Override
    public List<Permission> getRolePermissions(Long roleId, String tenantId) {
        // Get role permission IDs
        List<Long> permissionIds = rolePermissionMapper.selectList(new LambdaQueryWrapper<RolePermission>()
                .eq(RolePermission::getRoleId, roleId)
                .eq(RolePermission::getTenantId, tenantId))
                .stream()
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());

        // Get permissions
        if (!permissionIds.isEmpty()) {
            return baseMapper.getPermissionsByIds(permissionIds, tenantId);
        }
        return List.of();
    }

    @Override
    public List<Permission> getUserPermissions(Long userId, String tenantId) {
        // Get user roles
        List<Role> roles = getUserRoles(userId, tenantId);
        if (roles.isEmpty()) {
            return List.of();
        }

        // Get role permission IDs
        List<Long> roleIds = roles.stream()
                .map(Role::getId)
                .collect(Collectors.toList());

        List<Long> permissionIds = rolePermissionMapper.selectList(new LambdaQueryWrapper<RolePermission>()
                .in(RolePermission::getRoleId, roleIds)
                .eq(RolePermission::getTenantId, tenantId))
                .stream()
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());

        // Get permissions
        if (!permissionIds.isEmpty()) {
            return baseMapper.getPermissionsByIds(permissionIds, tenantId);
        }
        return List.of();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRole(Long userId, Long roleId, String tenantId) {
        // Check role exists
        Role role = getById(roleId);
        if (role == null) {
            throw new GlobalException(ResultCode.VALIDATE_FAILED, "Role not found");
        }

        // Check role belongs to tenant
        if (!tenantId.equals(role.getTenantId())) {
            throw new GlobalException(ResultCode.VALIDATE_FAILED, "Invalid tenant");
        }

        // Check user role not exists
        if (userRoleMapper.exists(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId)
                .eq(UserRole::getRoleId, roleId)
                .eq(UserRole::getTenantId, tenantId))) {
            return;
        }

        // Create user role
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRole.setTenantId(tenantId);
        userRoleMapper.insert(userRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRole(Long userId, Long roleId, String tenantId) {
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId)
                .eq(UserRole::getRoleId, roleId)
                .eq(UserRole::getTenantId, tenantId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermission(Long roleId, Long permissionId, String tenantId) {
        // Check permission exists
        Permission permission = baseMapper.getPermissionById(permissionId);
        if (permission == null) {
            throw new GlobalException(ResultCode.VALIDATE_FAILED, "Permission not found");
        }

        // Check permission belongs to tenant
        if (!tenantId.equals(permission.getTenantId())) {
            throw new GlobalException(ResultCode.VALIDATE_FAILED, "Invalid tenant");
        }

        // Check role permission not exists
        if (rolePermissionMapper.exists(new LambdaQueryWrapper<RolePermission>()
                .eq(RolePermission::getRoleId, roleId)
                .eq(RolePermission::getPermissionId, permissionId)
                .eq(RolePermission::getTenantId, tenantId))) {
            return;
        }

        // Create role permission
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRoleId(roleId);
        rolePermission.setPermissionId(permissionId);
        rolePermission.setTenantId(tenantId);
        rolePermissionMapper.insert(rolePermission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removePermission(Long roleId, Long permissionId, String tenantId) {
        rolePermissionMapper.delete(new LambdaQueryWrapper<RolePermission>()
                .eq(RolePermission::getRoleId, roleId)
                .eq(RolePermission::getPermissionId, permissionId)
                .eq(RolePermission::getTenantId, tenantId));
    }
} 