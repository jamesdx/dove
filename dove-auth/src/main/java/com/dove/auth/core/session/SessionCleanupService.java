package com.dove.auth.core.session;

import com.dove.auth.core.session.repository.RedisSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 会话清理服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SessionCleanupService {

    private final RedisSessionRepository sessionRepository;

    /**
     * 定时清理过期会话
     */
    @Scheduled(fixedRate = 300000) // 每5分钟执行一次
    public void cleanupExpiredSessions() {
        try {
            log.info("开始清理过期会话");
            sessionRepository.cleanupExpiredSessions();
            log.info("过期会话清理完成");
        } catch (Exception e) {
            log.error("清理过期会话异常", e);
        }
    }
} 