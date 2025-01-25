package com.helix.dove.auth.service;

import java.util.List;

public interface SessionService {
    
    /**
     * 创建会话
     *
     * @param userId 用户ID
     * @param deviceInfo 设备信息
     * @return 会话ID
     */
    String createSession(Long userId, DeviceInfo deviceInfo);
    
    /**
     * 获取用户的所有活跃会话
     *
     * @param userId 用户ID
     * @return 会话列表
     */
    List<SessionInfo> getActiveSessions(Long userId);
    
    /**
     * 终止会话
     *
     * @param sessionId 会话ID
     */
    void terminateSession(String sessionId);
    
    /**
     * 终止用户的所有会话（除了当前会话）
     *
     * @param userId 用户ID
     * @param currentSessionId 当前会话ID
     */
    void terminateOtherSessions(Long userId, String currentSessionId);
    
    /**
     * 更新会话最后活动时间
     *
     * @param sessionId 会话ID
     */
    void updateLastActivity(String sessionId);
}

record DeviceInfo(
    String deviceType,
    String deviceModel,
    String operatingSystem,
    String browser,
    String ipAddress,
    String location
) {}

record SessionInfo(
    String sessionId,
    DeviceInfo deviceInfo,
    long createdAt,
    long lastActivityAt,
    boolean current
) {} 