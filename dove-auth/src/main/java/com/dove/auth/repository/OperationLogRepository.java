package com.dove.auth.repository;

import com.dove.auth.domain.entity.OperationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志Repository
 */
public interface OperationLogRepository extends JpaRepository<OperationLogEntity, Long>, JpaSpecificationExecutor<OperationLogEntity> {

    /**
     * 根据用户ID查询列表
     */
    List<OperationLogEntity> findByUserId(Long userId);

    /**
     * 根据模块查询列表
     */
    List<OperationLogEntity> findByModule(String module);

    /**
     * 根据操作类型查询列表
     */
    List<OperationLogEntity> findByOperation(String operation);

    /**
     * 根据IP查询列表
     */
    List<OperationLogEntity> findByIp(String ip);

    /**
     * 根据状态查询列表
     */
    List<OperationLogEntity> findByStatus(Integer status);

    /**
     * 根据时间范围查询列表
     */
    List<OperationLogEntity> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据用户ID和模块查询列表
     */
    List<OperationLogEntity> findByUserIdAndModule(Long userId, String module);

    /**
     * 根据用户ID和操作类型查询列表
     */
    List<OperationLogEntity> findByUserIdAndOperation(Long userId, String operation);

    /**
     * 根据用户ID和状态查询列表
     */
    List<OperationLogEntity> findByUserIdAndStatus(Long userId, Integer status);

    /**
     * 根据用户ID和时间范围查询列表
     */
    List<OperationLogEntity> findByUserIdAndCreateTimeBetween(Long userId, LocalDateTime startTime, LocalDateTime endTime);
} 