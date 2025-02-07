package com.dove.auth.core.audit.service;

import com.dove.auth.core.audit.model.AuditEvent;
import com.dove.common.core.utils.JsonUtils;
import com.dove.common.core.utils.ServletUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 审计服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String AUDIT_KEY_PREFIX = "dove:auth:audit:";
    private static final long AUDIT_TTL = 7; // 审计日志保存7天

    /**
     * 记录审计事件
     */
    @Async
    public void log(String type, Long userId, String username, String result, String details) {
        try {
            // 创建审计事件
            AuditEvent event = new AuditEvent()
                .setId(UUID.randomUUID().toString())
                .setType(type)
                .setUserId(userId)
                .setUsername(username)
                .setIp(ServletUtils.getClientIP())
                .setTimestamp(LocalDateTime.now())
                .setResult(result)
                .setDetails(details)
                .setUserAgent(ServletUtils.getUserAgent())
                .setRequestUri(ServletUtils.getRequest().getRequestURI())
                .setMethod(ServletUtils.getRequest().getMethod())
                .setParams(JsonUtils.toJson(ServletUtils.getParameterMap()));

            // 保存审计事件
            saveAuditEvent(event);

            log.debug("审计事件记录成功: {}", event.getId());
        } catch (Exception e) {
            log.error("审计事件记录失败", e);
        }
    }

    /**
     * 记录异常审计事件
     */
    @Async
    public void logError(String type, Long userId, String username, String details, Exception ex) {
        try {
            // 创建审计事件
            AuditEvent event = new AuditEvent()
                .setId(UUID.randomUUID().toString())
                .setType(type)
                .setUserId(userId)
                .setUsername(username)
                .setIp(ServletUtils.getClientIP())
                .setTimestamp(LocalDateTime.now())
                .setResult("失败")
                .setDetails(details)
                .setUserAgent(ServletUtils.getUserAgent())
                .setRequestUri(ServletUtils.getRequest().getRequestURI())
                .setMethod(ServletUtils.getRequest().getMethod())
                .setParams(JsonUtils.toJson(ServletUtils.getParameterMap()))
                .setException(ex.getMessage());

            // 保存审计事件
            saveAuditEvent(event);

            log.debug("异常审计事件记录成功: {}", event.getId());
        } catch (Exception e) {
            log.error("异常审计事件记录失败", e);
        }
    }

    /**
     * 保存审计事件
     */
    private void saveAuditEvent(AuditEvent event) {
        String key = AUDIT_KEY_PREFIX + event.getId();
        redisTemplate.opsForValue().set(key, JsonUtils.toJson(event), AUDIT_TTL, TimeUnit.DAYS);
    }
} 