package com.dove.auth.repository;

import com.dove.auth.domain.entity.LoginLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 登录日志 Repository
 */
public interface LoginLogRepository extends JpaRepository<LoginLogEntity, Long>, JpaSpecificationExecutor<LoginLogEntity> {

    /**
     * 根据用户ID查询列表
     */
    List<LoginLogEntity> findByUserId(Long userId);

    /**
     * 根据登录类型查询列表
     */
    List<LoginLogEntity> findByLoginType(String loginType);

    /**
     * 根据登录IP查询列表
     */
    List<LoginLogEntity> findByLoginIp(String loginIp);

    /**
     * 根据设备ID查询列表
     */
    List<LoginLogEntity> findByDeviceId(String deviceId);

    /**
     * 根据状态查询列表
     */
    List<LoginLogEntity> findByStatus(Integer status);

    /**
     * 根据时间范围查询列表
     */
    List<LoginLogEntity> findByLoginTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据用户ID和状态查询列表
     */
    List<LoginLogEntity> findByUserIdAndStatus(Long userId, Integer status);

    /**
     * 根据用户ID和时间范围查询列表
     */
    List<LoginLogEntity> findByUserIdAndLoginTimeBetween(Long userId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据用户ID和登录类型查询列表
     */
    List<LoginLogEntity> findByUserIdAndLoginType(Long userId, String loginType);
} 