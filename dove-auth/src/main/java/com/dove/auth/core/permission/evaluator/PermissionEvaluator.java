package com.dove.auth.core.permission.evaluator;

import com.dove.auth.core.permission.service.PermissionService;
import com.dove.common.core.model.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * 权限评估器
 */
@Component
@RequiredArgsConstructor
public class PermissionEvaluator implements org.springframework.security.access.PermissionEvaluator {

    private final PermissionService permissionService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || targetDomainObject == null || !(permission instanceof String)) {
            return false;
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String permissionValue = (String) permission;
        String targetType = targetDomainObject.getClass().getSimpleName();

        return permissionService.hasPermission(loginUser.getUserId(), targetType, permissionValue);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication == null || targetType == null || !(permission instanceof String)) {
            return false;
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String permissionValue = (String) permission;

        return permissionService.hasPermission(loginUser.getUserId(), targetType, permissionValue);
    }
} 