package com.helix.dove.auth.service;

import com.helix.dove.auth.entity.UserSession;

import java.util.List;

/**
 * User Session Service Interface
 */
public interface UserSessionService {

    /**
     * Create user session
     *
     * @param userId user ID
     * @param token token
     * @param refreshToken refresh token
     * @param deviceInfo device info
     * @param loginIp login IP
     * @param tenantId tenant ID
     * @param rememberMe remember me
     * @return user session
     */
    UserSession createSession(Long userId, String token, String refreshToken, String deviceInfo, 
            String loginIp, String tenantId, boolean rememberMe);

    /**
     * Get user session by token
     *
     * @param token token
     * @return user session
     */
    UserSession getSessionByToken(String token);

    /**
     * Get user session by refresh token
     *
     * @param refreshToken refresh token
     * @return user session
     */
    UserSession getSessionByRefreshToken(String refreshToken);

    /**
     * Get user sessions
     *
     * @param userId user ID
     * @param tenantId tenant ID
     * @return user session list
     */
    List<UserSession> getUserSessions(Long userId, String tenantId);

    /**
     * Refresh user session
     *
     * @param refreshToken refresh token
     * @return new user session
     */
    UserSession refreshSession(String refreshToken);

    /**
     * Invalidate user session
     *
     * @param token token
     */
    void invalidateSession(String token);

    /**
     * Invalidate all user sessions
     *
     * @param userId user ID
     * @param tenantId tenant ID
     */
    void invalidateUserSessions(Long userId, String tenantId);

    /**
     * Update last access time
     *
     * @param token token
     */
    void updateLastAccessTime(String token);

    /**
     * Clean expired sessions
     */
    void cleanExpiredSessions();
} 