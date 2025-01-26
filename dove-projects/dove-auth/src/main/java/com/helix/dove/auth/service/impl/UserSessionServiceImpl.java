package com.helix.dove.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.helix.dove.auth.entity.UserSession;
import com.helix.dove.auth.mapper.UserSessionMapper;
import com.helix.dove.auth.service.UserSessionService;
import com.helix.dove.auth.util.JwtTokenUtil;
import com.helix.dove.common.api.ResultCode;
import com.helix.dove.common.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * User Session Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserSessionServiceImpl extends ServiceImpl<UserSessionMapper, UserSession> implements UserSessionService {

    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.expiration}")
    private Long expiration;

    private static final String TOKEN_KEY_PREFIX = "token:";
    private static final String REFRESH_TOKEN_KEY_PREFIX = "refresh_token:";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserSession createSession(Long userId, String token, String refreshToken, String deviceInfo,
            String loginIp, String tenantId, boolean rememberMe) {
        // Calculate expiration time
        long tokenExpiration = rememberMe ? expiration * 7 : expiration;
        long refreshTokenExpiration = tokenExpiration * 2;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tokenExpirationTime = now.plusSeconds(tokenExpiration);
        LocalDateTime refreshTokenExpirationTime = now.plusSeconds(refreshTokenExpiration);

        // Create session
        UserSession session = new UserSession();
        session.setUserId(userId);
        session.setToken(token);
        session.setRefreshToken(refreshToken);
        session.setTokenExpirationTime(tokenExpirationTime);
        session.setRefreshTokenExpirationTime(refreshTokenExpirationTime);
        session.setDeviceInfo(deviceInfo);
        session.setLoginIp(loginIp);
        session.setLastAccessTime(now);
        session.setStatus(1);
        session.setTenantId(tenantId);

        // Save session
        save(session);

        // Cache token and refresh token
        redisTemplate.opsForValue().set(TOKEN_KEY_PREFIX + token, session.getId(), tokenExpiration, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(REFRESH_TOKEN_KEY_PREFIX + refreshToken, session.getId(), 
                refreshTokenExpiration, TimeUnit.SECONDS);

        return session;
    }

    @Override
    public UserSession getSessionByToken(String token) {
        Object sessionId = redisTemplate.opsForValue().get(TOKEN_KEY_PREFIX + token);
        if (sessionId == null) {
            return null;
        }
        return getById((Long) sessionId);
    }

    @Override
    public UserSession getSessionByRefreshToken(String refreshToken) {
        Object sessionId = redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY_PREFIX + refreshToken);
        if (sessionId == null) {
            return null;
        }
        return getById((Long) sessionId);
    }

    @Override
    public List<UserSession> getUserSessions(Long userId, String tenantId) {
        return list(new LambdaQueryWrapper<UserSession>()
                .eq(UserSession::getUserId, userId)
                .eq(UserSession::getTenantId, tenantId)
                .eq(UserSession::getStatus, 1)
                .orderByDesc(UserSession::getLastAccessTime));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserSession refreshSession(String refreshToken) {
        // Get session by refresh token
        UserSession session = getSessionByRefreshToken(refreshToken);
        if (session == null || session.getStatus() == 0 || 
                session.getRefreshTokenExpirationTime().isBefore(LocalDateTime.now())) {
            throw new GlobalException(ResultCode.UNAUTHORIZED, "Invalid refresh token");
        }

        // Generate new tokens
        String newToken = jwtTokenUtil.generateToken(session.getUserId().toString(), session.getTenantId(), false);
        String newRefreshToken = UUID.randomUUID().toString().replace("-", "");

        // Calculate expiration time
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tokenExpirationTime = now.plusSeconds(expiration);
        LocalDateTime refreshTokenExpirationTime = now.plusSeconds(expiration * 2);

        // Update session
        session.setToken(newToken);
        session.setRefreshToken(newRefreshToken);
        session.setTokenExpirationTime(tokenExpirationTime);
        session.setRefreshTokenExpirationTime(refreshTokenExpirationTime);
        session.setLastAccessTime(now);
        updateById(session);

        // Update cache
        redisTemplate.delete(TOKEN_KEY_PREFIX + session.getToken());
        redisTemplate.delete(REFRESH_TOKEN_KEY_PREFIX + refreshToken);
        redisTemplate.opsForValue().set(TOKEN_KEY_PREFIX + newToken, session.getId(), expiration, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(REFRESH_TOKEN_KEY_PREFIX + newRefreshToken, session.getId(), 
                expiration * 2, TimeUnit.SECONDS);

        return session;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void invalidateSession(String token) {
        UserSession session = getSessionByToken(token);
        if (session != null) {
            // Update session status
            session.setStatus(0);
            updateById(session);

            // Remove cache
            redisTemplate.delete(TOKEN_KEY_PREFIX + token);
            redisTemplate.delete(REFRESH_TOKEN_KEY_PREFIX + session.getRefreshToken());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void invalidateUserSessions(Long userId, String tenantId) {
        // Get user sessions
        List<UserSession> sessions = getUserSessions(userId, tenantId);

        // Update session status
        for (UserSession session : sessions) {
            session.setStatus(0);
            updateById(session);

            // Remove cache
            redisTemplate.delete(TOKEN_KEY_PREFIX + session.getToken());
            redisTemplate.delete(REFRESH_TOKEN_KEY_PREFIX + session.getRefreshToken());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLastAccessTime(String token) {
        UserSession session = getSessionByToken(token);
        if (session != null) {
            session.setLastAccessTime(LocalDateTime.now());
            updateById(session);
        }
    }

    @Override
    @Scheduled(cron = "0 0 * * * ?") // Run every hour
    @Transactional(rollbackFor = Exception.class)
    public void cleanExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();

        // Get expired sessions
        List<UserSession> expiredSessions = list(new LambdaQueryWrapper<UserSession>()
                .eq(UserSession::getStatus, 1)
                .lt(UserSession::getRefreshTokenExpirationTime, now));

        // Update session status and remove cache
        for (UserSession session : expiredSessions) {
            session.setStatus(0);
            updateById(session);

            redisTemplate.delete(TOKEN_KEY_PREFIX + session.getToken());
            redisTemplate.delete(REFRESH_TOKEN_KEY_PREFIX + session.getRefreshToken());
        }
    }
} 