package com.helix.dove.auth.service.impl;

import com.helix.dove.auth.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String SESSION_KEY_PREFIX = "session:";
    private static final String USER_SESSIONS_KEY_PREFIX = "user:sessions:";
    private static final long SESSION_TIMEOUT = 30; // 30天

    @Override
    public String createSession(Long userId, DeviceInfo deviceInfo) {
        String sessionId = UUID.randomUUID().toString();
        String sessionKey = SESSION_KEY_PREFIX + sessionId;
        String userSessionsKey = USER_SESSIONS_KEY_PREFIX + userId;

        SessionInfo sessionInfo = new SessionInfo(
            sessionId,
            deviceInfo,
            Instant.now().getEpochSecond(),
            Instant.now().getEpochSecond(),
            true
        );

        redisTemplate.opsForValue().set(sessionKey, sessionInfo, SESSION_TIMEOUT, TimeUnit.DAYS);
        redisTemplate.opsForSet().add(userSessionsKey, sessionId);
        redisTemplate.expire(userSessionsKey, SESSION_TIMEOUT, TimeUnit.DAYS);

        return sessionId;
    }

    @Override
    public List<SessionInfo> getActiveSessions(Long userId) {
        String userSessionsKey = USER_SESSIONS_KEY_PREFIX + userId;
        Set<Object> sessionIds = redisTemplate.opsForSet().members(userSessionsKey);
        
        if (sessionIds == null) {
            return new ArrayList<>();
        }

        List<SessionInfo> sessions = new ArrayList<>();
        for (Object sessionId : sessionIds) {
            String sessionKey = SESSION_KEY_PREFIX + sessionId;
            SessionInfo sessionInfo = (SessionInfo) redisTemplate.opsForValue().get(sessionKey);
            if (sessionInfo != null) {
                sessions.add(sessionInfo);
            } else {
                // 清理无效的会话ID
                redisTemplate.opsForSet().remove(userSessionsKey, sessionId);
            }
        }

        return sessions;
    }

    @Override
    public void terminateSession(String sessionId) {
        String sessionKey = SESSION_KEY_PREFIX + sessionId;
        SessionInfo sessionInfo = (SessionInfo) redisTemplate.opsForValue().get(sessionKey);
        
        if (sessionInfo != null) {
            // 从用户的会话集合中移除
            String userSessionsKey = USER_SESSIONS_KEY_PREFIX + sessionInfo.sessionId();
            redisTemplate.opsForSet().remove(userSessionsKey, sessionId);
        }

        redisTemplate.delete(sessionKey);
    }

    @Override
    public void terminateOtherSessions(Long userId, String currentSessionId) {
        String userSessionsKey = USER_SESSIONS_KEY_PREFIX + userId;
        Set<Object> sessionIds = redisTemplate.opsForSet().members(userSessionsKey);
        
        if (sessionIds != null) {
            for (Object sessionId : sessionIds) {
                if (!sessionId.equals(currentSessionId)) {
                    terminateSession((String) sessionId);
                }
            }
        }
    }

    @Override
    public void updateLastActivity(String sessionId) {
        String sessionKey = SESSION_KEY_PREFIX + sessionId;
        SessionInfo sessionInfo = (SessionInfo) redisTemplate.opsForValue().get(sessionKey);
        
        if (sessionInfo != null) {
            SessionInfo updatedInfo = new SessionInfo(
                sessionInfo.sessionId(),
                sessionInfo.deviceInfo(),
                sessionInfo.createdAt(),
                Instant.now().getEpochSecond(),
                sessionInfo.current()
            );
            
            redisTemplate.opsForValue().set(sessionKey, updatedInfo, SESSION_TIMEOUT, TimeUnit.DAYS);
        }
    }
} 