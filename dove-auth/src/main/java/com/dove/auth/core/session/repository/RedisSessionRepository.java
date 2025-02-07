package com.dove.auth.core.session.repository;

import com.dove.auth.core.session.model.Session;
import com.dove.common.core.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Redis会话仓库
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSessionRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String SESSION_KEY_PREFIX = "dove:auth:session:";
    private static final String USER_SESSION_KEY_PREFIX = "dove:auth:user_session:";

    /**
     * 保存会话
     */
    public void save(Session session) {
        try {
            // 1. 序列化会话数据
            String sessionJson = JsonUtils.toJson(session);
            String sessionKey = getSessionKey(session.getId());
            String userSessionKey = getUserSessionKey(session.getUserId());

            // 2. 设置过期时间
            Duration timeout = Duration.between(LocalDateTime.now(), session.getExpireTime());

            // 3. 保存到Redis
            redisTemplate.opsForValue().set(sessionKey, sessionJson, timeout);
            redisTemplate.opsForSet().add(userSessionKey, session.getId());

            log.debug("会话保存成功: {}", session.getId());
        } catch (Exception e) {
            log.error("会话保存失败: {}", session.getId(), e);
            throw new RuntimeException("会话保存失败", e);
        }
    }

    /**
     * 查找会话
     */
    public Session findById(String id) {
        try {
            // 1. 从Redis读取数据
            String sessionKey = getSessionKey(id);
            String sessionJson = (String) redisTemplate.opsForValue().get(sessionKey);
            if (sessionJson == null) {
                return null;
            }

            // 2. 反序列化会话对象
            Session session = JsonUtils.fromJson(sessionJson, Session.class);
            if (session == null) {
                return null;
            }

            // 3. 更新访问时间
            if (!session.isExpired()) {
                session.touch();
                save(session);
            }

            return session;
        } catch (Exception e) {
            log.error("会话查找失败: {}", id, e);
            return null;
        }
    }

    /**
     * 删除会话
     */
    public void deleteById(String id) {
        try {
            Session session = findById(id);
            if (session != null) {
                String sessionKey = getSessionKey(id);
                String userSessionKey = getUserSessionKey(session.getUserId());
                redisTemplate.delete(sessionKey);
                redisTemplate.opsForSet().remove(userSessionKey, id);
                log.debug("会话删除成功: {}", id);
            }
        } catch (Exception e) {
            log.error("会话删除失败: {}", id, e);
        }
    }

    /**
     * 获取用户的所有会话ID
     */
    public Set<String> findSessionIdsByUserId(Long userId) {
        String userSessionKey = getUserSessionKey(userId);
        Set<Object> sessionIds = redisTemplate.opsForSet().members(userSessionKey);
        return sessionIds != null ? 
               sessionIds.stream().map(Object::toString).collect(Collectors.toSet()) : 
               Set.of();
    }

    /**
     * 获取所有会话ID
     */
    public Set<String> getAllSessionIds() {
        Set<String> keys = redisTemplate.keys(SESSION_KEY_PREFIX + "*");
        return keys != null ? 
               keys.stream()
                   .map(key -> key.substring(SESSION_KEY_PREFIX.length()))
                   .collect(Collectors.toSet()) : 
               Set.of();
    }

    /**
     * 清理过期会话
     */
    public void cleanupExpiredSessions() {
        try {
            Set<String> allSessionIds = getAllSessionIds();
            for (String id : allSessionIds) {
                Session session = findById(id);
                if (session != null && session.isExpired()) {
                    deleteById(id);
                }
            }
            log.info("过期会话清理完成");
        } catch (Exception e) {
            log.error("过期会话清理失败", e);
        }
    }

    private String getSessionKey(String sessionId) {
        return SESSION_KEY_PREFIX + sessionId;
    }

    private String getUserSessionKey(Long userId) {
        return USER_SESSION_KEY_PREFIX + userId;
    }
} 