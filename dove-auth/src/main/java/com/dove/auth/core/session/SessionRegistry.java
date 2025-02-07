package com.dove.auth.core.session;

import com.dove.common.core.model.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 会话注册表
 */
@Component
@RequiredArgsConstructor
public class SessionRegistry {

    private final RedisTemplate<String, Object> redisTemplate;
    private final SessionProperties sessionProperties;

    /**
     * 注册会话
     */
    public void registerNewSession(String sessionId, LoginUser loginUser) {
        String sessionKey = getSessionKey(sessionId);
        String userSessionKey = getUserSessionKey(loginUser.getUserId());

        // 检查是否超过最大会话数
        if (!sessionProperties.isConcurrentLoginPermitted()) {
            Set<Object> existingSessions = redisTemplate.opsForSet().members(userSessionKey);
            if (existingSessions != null && !existingSessions.isEmpty()) {
                if (sessionProperties.isMaxSessionsPreventsLogin()) {
                    throw new IllegalStateException("Maximum sessions of " + sessionProperties.getMaximumSessions() + " for this principal exceeded");
                }
                // 移除旧会话
                existingSessions.forEach(oldSessionId -> removeSession((String) oldSessionId));
            }
        }

        // 保存会话信息
        redisTemplate.opsForValue().set(sessionKey, loginUser, sessionProperties.getTimeout(), TimeUnit.MINUTES);
        redisTemplate.opsForSet().add(userSessionKey, sessionId);
    }

    /**
     * 移除会话
     */
    public void removeSession(String sessionId) {
        String sessionKey = getSessionKey(sessionId);
        LoginUser loginUser = getLoginUser(sessionId);
        if (loginUser != null) {
            String userSessionKey = getUserSessionKey(loginUser.getUserId());
            redisTemplate.opsForSet().remove(userSessionKey, sessionId);
        }
        redisTemplate.delete(sessionKey);
    }

    /**
     * 获取会话信息
     */
    public LoginUser getLoginUser(String sessionId) {
        String sessionKey = getSessionKey(sessionId);
        return (LoginUser) redisTemplate.opsForValue().get(sessionKey);
    }

    /**
     * 获取用户的所有会话
     */
    public List<String> getUserSessions(Long userId) {
        String userSessionKey = getUserSessionKey(userId);
        Set<Object> sessions = redisTemplate.opsForSet().members(userSessionKey);
        return sessions != null ? new ArrayList<>(sessions.stream().map(Object::toString).toList()) : new ArrayList<>();
    }

    /**
     * 更新会话
     */
    public void refreshSession(String sessionId) {
        String sessionKey = getSessionKey(sessionId);
        redisTemplate.expire(sessionKey, sessionProperties.getTimeout(), TimeUnit.MINUTES);
    }

    /**
     * 获取所有会话
     */
    public List<String> getAllSessions() {
        String pattern = getSessionKey("*");
        Set<String> keys = redisTemplate.keys(pattern);
        return keys != null ? new ArrayList<>(keys) : new ArrayList<>();
    }

    /**
     * 获取会话数量
     */
    public long getSessionCount() {
        String pattern = getSessionKey("*");
        Set<String> keys = redisTemplate.keys(pattern);
        return keys != null ? keys.size() : 0;
    }

    /**
     * 获取用户会话数量
     */
    public long getUserSessionCount(Long userId) {
        String userSessionKey = getUserSessionKey(userId);
        Long size = redisTemplate.opsForSet().size(userSessionKey);
        return size != null ? size : 0;
    }

    /**
     * 判断会话是否存在
     */
    public boolean sessionExists(String sessionId) {
        String sessionKey = getSessionKey(sessionId);
        return Boolean.TRUE.equals(redisTemplate.hasKey(sessionKey));
    }

    /**
     * 获取会话key
     */
    private String getSessionKey(String sessionId) {
        return sessionProperties.getRedisKeyPrefix() + "session:" + sessionId;
    }

    /**
     * 获取用户会话key
     */
    private String getUserSessionKey(Long userId) {
        return sessionProperties.getRedisKeyPrefix() + "user:" + userId;
    }
} 