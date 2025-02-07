package com.dove.auth.repository;

import com.dove.auth.domain.entity.SecurityAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 安全审计Repository
 */
public interface SecurityAuditRepository extends JpaRepository<SecurityAuditEntity, Long>, JpaSpecificationExecutor<SecurityAuditEntity> {

    /**
     * 根据用户ID查询列表
     */
    List<SecurityAuditEntity> findByUserId(Long userId);

    /**
     * 根据事件类型查询列表
     */
    List<SecurityAuditEntity> findByEventType(String eventType);

    /**
     * 根据IP地址查询列表
     */
    List<SecurityAuditEntity> findByIp(String ip);

    /**
     * 根据风险等级查询列表
     */
    List<SecurityAuditEntity> findByRiskLevel(Integer riskLevel);

    /**
     * 根据时间范围查询列表
     */
    List<SecurityAuditEntity> findByEventTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据用户ID和事件类型查询列表
     */
    List<SecurityAuditEntity> findByUserIdAndEventType(Long userId, String eventType);

    /**
     * 根据用户ID和时间范围查询列表
     */
    List<SecurityAuditEntity> findByUserIdAndEventTimeBetween(Long userId, LocalDateTime startTime, LocalDateTime endTime);
} 