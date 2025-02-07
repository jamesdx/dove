package com.dove.auth.core.permission.service;

import com.dove.user.api.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 权限服务
 */
@Service
@RequiredArgsConstructor
public class PermissionService {

    private final UserServiceClient userServiceClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PERMISSION_CACHE_PREFIX = "dove:auth:permission:";
    private static final long PERMISSION_CACHE_TTL = 30; // 缓存30分钟

    /**
     * 判断用户是否具有指定权限
     */
    public boolean hasPermission(Long userId, String targetType, String permission) {
        // 1. 从缓存获取权限
        String cacheKey = getPermissionCacheKey(userId);
        Set<String> permissions = (Set<String>) redisTemplate.opsForValue().get(cacheKey);

        // 2. 缓存未命中，从用户服务获取权限
        if (permissions == null) {
            permissions = userServiceClient.getUserPermissions(userId);
            if (permissions != null) {
                redisTemplate.opsForValue().set(cacheKey, permissions, PERMISSION_CACHE_TTL, TimeUnit.MINUTES);
            }
        }

        // 3. 检查权限
        if (permissions != null) {
            String fullPermission = targetType + ":" + permission;
            return permissions.contains(fullPermission) || permissions.contains("*:*");
        }

        return false;
    }

    /**
     * 刷新用户权限缓存
     */
    public void refreshPermissionCache(Long userId) {
        String cacheKey = getPermissionCacheKey(userId);
        Set<String> permissions = userServiceClient.getUserPermissions(userId);
        if (permissions != null) {
            redisTemplate.opsForValue().set(cacheKey, permissions, PERMISSION_CACHE_TTL, TimeUnit.MINUTES);
        }
    }

    /**
     * 清除用户权限缓存
     */
    public void clearPermissionCache(Long userId) {
        String cacheKey = getPermissionCacheKey(userId);
        redisTemplate.delete(cacheKey);
    }

    private String getPermissionCacheKey(Long userId) {
        return PERMISSION_CACHE_PREFIX + userId;
    }
} 