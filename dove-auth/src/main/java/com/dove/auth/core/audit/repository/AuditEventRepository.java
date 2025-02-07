package com.dove.auth.core.audit.repository;

import com.dove.auth.core.audit.model.AuditEvent;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 审计事件仓库接口
 */
public interface AuditEventRepository {

    /**
     * 保存审计事件
     */
    void save(AuditEvent event);

    /**
     * 根据ID查询审计事件
     */
    AuditEvent findById(String id);

    /**
     * 根据用户ID查询审计事件
     */
    List<AuditEvent> findByUserId(Long userId, int limit);

    /**
     * 查询时间范围内的审计事件
     */
    List<AuditEvent> findByTimeRange(LocalDateTime start, LocalDateTime end);

    /**
     * 查询指定类型的审计事件
     */
    List<AuditEvent> findByType(String type, int limit);

    /**
     * 删除指定时间之前的审计事件
     */
    void deleteBeforeTime(LocalDateTime time);

    /**
     * 清理过期的审计事件
     */
    void cleanupExpiredEvents();
} 