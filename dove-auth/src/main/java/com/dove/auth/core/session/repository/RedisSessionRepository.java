package com.dove.auth.core.session.repository;

import com.dove.auth.core.session.model.SessionInformation;
import com.dove.common.core.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Redis会话仓库
 */
@Component
@RequiredArgsConstructor
public class RedisSessionRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String SESSION_KEY_PREFIX = "dove:auth:session:";
    private static final String USER_SESSION_KEY_PREFIX = "dove:auth:user_session:";

    /**
     * 保存会话
     */
    public void save(SessionInformation session) {
        String sessionKey = getSessionKey(session.getSessionId());
        String userSessionKey = getUserSessionKey(session.getLoginUser().getUserId());

        // 保存会话信息
        redisTemplate.opsForValue().set(sessionKey, JsonUtils.toJson(session));
        // 设置过期时间
        if (session.getExpireTime() != null) {
            Duration duration = Duration.between(session.getLoginTime(), session.getExpireTime());
            redisTemplate.expire(sessionKey, duration);
        }

        // 关联用户和会话
        redisTemplate.opsForSet().add(userSessionKey, session.getSessionId());
    }

    /**
     * 获取会话
     */
    public SessionInformation findById(String sessionId) {
        String sessionKey = getSessionKey(sessionId);
        String json = (String) redisTemplate.opsForValue().get(sessionKey);
        return json != null ? JsonUtils.fromJson(json, SessionInformation.class) : null;
    }

    /**
     * 获取用户的所有会话
     */
    public Set<SessionInformation> findByUserId(Long userId) {
        String userSessionKey = getUserSessionKey(userId);
        Set<Object> sessionIds = redisTemplate.opsForSet().members(userSessionKey);
        if (sessionIds == null) {
            return Set.of();
        }
        return sessionIds.stream()
                .map(id -> findById((String) id))
                .filter(session -> session != null && !session.isExpired())
                .collect(Collectors.toSet());
    }

    /**
     * 删除会话
     */
    public void deleteById(String sessionId) {
        SessionInformation session = findById(sessionId);
        if (session != null) {
            String sessionKey = getSessionKey(sessionId);
            String userSessionKey = getUserSessionKey(session.getLoginUser().getUserId());
            redisTemplate.delete(sessionKey);
            redisTemplate.opsForSet().remove(userSessionKey, sessionId);
        }
    }

    /**
     * 清理过期会话
     */
    public void cleanupExpiredSessions() {
        // 获取所有会话key
        Set<String> keys = redisTemplate.keys(SESSION_KEY_PREFIX + "*");
        if (keys != null) {
            for (String key : keys) {
                String json = (String) redisTemplate.opsForValue().get(key);
                if (json != null) {
                    SessionInformation session = JsonUtils.fromJson(json, SessionInformation.class);
                    if (session != null && session.isExpired()) {
                        deleteById(session.getSessionId());
                    }
                }
            }
        }
    }

    private String getSessionKey(String sessionId) {
        return SESSION_KEY_PREFIX + sessionId;
    }

    private String getUserSessionKey(Long userId) {
        return USER_SESSION_KEY_PREFIX + userId;
    }
} 