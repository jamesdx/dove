package com.dove.auth.core.permission.aspect;

import com.dove.auth.core.permission.annotation.Logical;
import com.dove.auth.core.permission.annotation.RequiresPermissions;
import com.dove.auth.core.permission.service.PermissionService;
import com.dove.auth.core.utils.SecurityUtils;
import com.dove.common.core.exception.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 权限注解切面
 */
@Aspect
@Component
@RequiredArgsConstructor
public class PermissionAspect {

    private final PermissionService permissionService;

    @Before("@annotation(com.dove.auth.core.permission.annotation.RequiresPermissions)")
    public void checkPermission(JoinPoint point) {
        // 获取用户ID
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            throw new AccessDeniedException("用户未登录");
        }

        // 获取注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        RequiresPermissions requiresPermissions = signature.getMethod().getAnnotation(RequiresPermissions.class);

        // 获取权限字符串
        String[] permissions = requiresPermissions.value();
        if (permissions.length == 0) {
            return;
        }

        // 验证权限
        if (requiresPermissions.logical() == Logical.AND) {
            checkPermissionsAnd(userId, permissions, requiresPermissions.message());
        } else {
            checkPermissionsOr(userId, permissions, requiresPermissions.message());
        }
    }

    /**
     * AND 验证
     */
    private void checkPermissionsAnd(Long userId, String[] permissions, String message) {
        for (String permission : permissions) {
            if (!permissionService.hasPermission(userId, "system", permission)) {
                throw new AccessDeniedException(message);
            }
        }
    }

    /**
     * OR 验证
     */
    private void checkPermissionsOr(Long userId, String[] permissions, String message) {
        for (String permission : permissions) {
            if (permissionService.hasPermission(userId, "system", permission)) {
                return;
            }
        }
        throw new AccessDeniedException(message);
    }
} 