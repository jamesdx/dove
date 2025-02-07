package com.dove.auth.core.session;

import com.dove.auth.core.session.model.SessionInformation;
import com.dove.auth.core.session.repository.RedisSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 会话监控
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SessionMonitor {

    private final RedisSessionRepository sessionRepository;
    private final Map<String, SessionMetrics> metricsCache = new ConcurrentHashMap<>();

    /**
     * 监控会话状态
     */
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void monitorSessions() {
        try {
            // 收集会话指标
            SessionMetrics currentMetrics = collectSessionMetrics();
            
            // 检查异常情况
            checkAbnormalSessions(currentMetrics);
            
            // 更新缓存
            updateMetricsCache(currentMetrics);
            
            // 记录监控日志
            logSessionMetrics(currentMetrics);
        } catch (Exception e) {
            log.error("会话监控异常", e);
        }
    }

    /**
     * 收集会话指标
     */
    private SessionMetrics collectSessionMetrics() {
        Set<String> allSessions = sessionRepository.getAllSessionIds();
        Set<SessionInformation> activeSessions = allSessions.stream()
                .map(sessionRepository::findById)
                .filter(session -> session != null && !session.isExpired())
                .collect(Collectors.toSet());

        SessionMetrics metrics = new SessionMetrics();
        metrics.setTotalSessions(allSessions.size());
        metrics.setActiveSessions(activeSessions.size());
        metrics.setExpiredSessions(allSessions.size() - activeSessions.size());
        
        // 统计每个用户的会话数
        Map<Long, Long> userSessionCounts = activeSessions.stream()
                .collect(Collectors.groupingBy(
                        session -> session.getLoginUser().getUserId(),
                        Collectors.counting()
                ));
        metrics.setMaxUserSessions(userSessionCounts.values().stream().mapToLong(Long::longValue).max().orElse(0));
        
        return metrics;
    }

    /**
     * 检查异常情况
     */
    private void checkAbnormalSessions(SessionMetrics currentMetrics) {
        SessionMetrics lastMetrics = metricsCache.get("latest");
        if (lastMetrics != null) {
            // 检查会话数突增
            if (currentMetrics.getActiveSessions() > lastMetrics.getActiveSessions() * 1.5) {
                log.warn("检测到会话数突增: {} -> {}", 
                        lastMetrics.getActiveSessions(), currentMetrics.getActiveSessions());
            }
            
            // 检查过期会话数突增
            if (currentMetrics.getExpiredSessions() > lastMetrics.getExpiredSessions() * 1.5) {
                log.warn("检测到过期会话数突增: {} -> {}", 
                        lastMetrics.getExpiredSessions(), currentMetrics.getExpiredSessions());
            }
        }
    }

    /**
     * 更新指标缓存
     */
    private void updateMetricsCache(SessionMetrics metrics) {
        metricsCache.put("latest", metrics);
    }

    /**
     * 记录监控日志
     */
    private void logSessionMetrics(SessionMetrics metrics) {
        log.info("会话监控指标 - 总会话数: {}, 活跃会话数: {}, 过期会话数: {}, 最大用户会话数: {}", 
                metrics.getTotalSessions(), metrics.getActiveSessions(), 
                metrics.getExpiredSessions(), metrics.getMaxUserSessions());
    }

    /**
     * 会话指标
     */
    @lombok.Data
    private static class SessionMetrics {
        private int totalSessions;
        private int activeSessions;
        private int expiredSessions;
        private long maxUserSessions;
    }
} 