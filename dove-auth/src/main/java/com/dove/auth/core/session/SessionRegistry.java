package com.dove.auth.core.session;

import com.dove.auth.core.session.model.SessionInformation;
import com.dove.auth.core.session.repository.RedisSessionRepository;
import com.dove.common.core.model.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * 会话注册表
 */
@Component
@RequiredArgsConstructor
public class SessionRegistry {

    private final RedisSessionRepository sessionRepository;
    private final SessionProperties sessionProperties;

    /**
     * 注册新会话
     */
    public SessionInformation registerNewSession(LoginUser loginUser, String loginIp, String deviceInfo) {
        // 创建会话信息
        SessionInformation session = new SessionInformation();
        session.setSessionId(UUID.randomUUID().toString());
        session.setLoginUser(loginUser);
        session.setLoginIp(loginIp);
        session.setDeviceInfo(deviceInfo);
        session.setLoginTime(LocalDateTime.now());
        session.setLastAccessTime(LocalDateTime.now());
        session.setExpireTime(LocalDateTime.now().plusMinutes(sessionProperties.getTimeout()));

        // 检查并发会话限制
        if (!sessionProperties.isConcurrentLoginPermitted()) {
            Set<SessionInformation> existingSessions = sessionRepository.findByUserId(loginUser.getUserId());
            if (!existingSessions.isEmpty()) {
                if (existingSessions.size() >= sessionProperties.getMaximumSessions()) {
                    throw new IllegalStateException("已达到最大会话数限制");
                }
                // 移除旧会话
                existingSessions.forEach(oldSession -> sessionRepository.deleteById(oldSession.getSessionId()));
            }
        }

        // 保存会话
        sessionRepository.save(session);
        return session;
    }

    /**
     * 获取会话信息
     */
    public SessionInformation getSessionInformation(String sessionId) {
        return sessionRepository.findById(sessionId);
    }

    /**
     * 获取用户的所有会话
     */
    public Set<SessionInformation> getAllSessions(Long userId) {
        return sessionRepository.findByUserId(userId);
    }

    /**
     * 更新会话访问时间
     */
    public void updateLastAccessTime(String sessionId) {
        SessionInformation session = getSessionInformation(sessionId);
        if (session != null && !session.isExpired()) {
            session.updateLastAccessTime();
            sessionRepository.save(session);
        }
    }

    /**
     * 使会话过期
     */
    public void expireNow(String sessionId) {
        SessionInformation session = getSessionInformation(sessionId);
        if (session != null) {
            session.setExpired(true);
            sessionRepository.save(session);
        }
    }

    /**
     * 移除会话
     */
    public void removeSessionInformation(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }
} 