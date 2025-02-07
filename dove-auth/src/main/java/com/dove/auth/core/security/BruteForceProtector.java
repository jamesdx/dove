package com.dove.auth.core.security;

import com.dove.auth.core.event.SecurityEventPublisher;
import com.dove.auth.core.event.SecurityEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 暴力破解防护
 */
@Component
@RequiredArgsConstructor
public class BruteForceProtector {

    private final RedisTemplate<String, Integer> redisTemplate;
    private final SecurityEventPublisher eventPublisher;

    /**
     * Redis key前缀
     */
    private static final String REDIS_KEY_PREFIX = "dove:auth:login:attempt:";

    /**
     * IP Redis key前缀
     */
    private static final String IP_REDIS_KEY_PREFIX = "dove:auth:login:ip:";

    /**
     * 最大尝试次数
     */
    private static final int MAX_ATTEMPTS = 5;

    /**
     * IP最大尝试次数
     */
    private static final int IP_MAX_ATTEMPTS = 20;

    /**
     * 锁定时间（分钟）
     */
    private static final int LOCK_TIME = 30;

    /**
     * IP锁定时间（分钟）
     */
    private static final int IP_LOCK_TIME = 60;

    /**
     * 记录登录失败
     */
    public void recordLoginFailure(String username, String ip) {
        // 记录用户名失败次数
        String userKey = REDIS_KEY_PREFIX + username;
        Integer attempts = redisTemplate.opsForValue().get(userKey);
        int userAttempts = attempts == null ? 1 : attempts + 1;
        redisTemplate.opsForValue().set(userKey, userAttempts, LOCK_TIME, TimeUnit.MINUTES);

        // 记录IP失败次数
        String ipKey = IP_REDIS_KEY_PREFIX + ip;
        Integer ipAttempts = redisTemplate.opsForValue().get(ipKey);
        int newIpAttempts = ipAttempts == null ? 1 : ipAttempts + 1;
        redisTemplate.opsForValue().set(ipKey, newIpAttempts, IP_LOCK_TIME, TimeUnit.MINUTES);

        // 检查是否需要锁定
        checkLock(username, userAttempts, ip, newIpAttempts);
    }

    /**
     * 检查是否被锁定
     */
    public boolean isLocked(String username, String ip) {
        // 检查用户名是否被锁定
        String userKey = REDIS_KEY_PREFIX + username;
        Integer attempts = redisTemplate.opsForValue().get(userKey);
        if (attempts != null && attempts >= MAX_ATTEMPTS) {
            return true;
        }

        // 检查IP是否被锁定
        String ipKey = IP_REDIS_KEY_PREFIX + ip;
        Integer ipAttempts = redisTemplate.opsForValue().get(ipKey);
        return ipAttempts != null && ipAttempts >= IP_MAX_ATTEMPTS;
    }

    /**
     * 重置登录失败次数
     */
    public void resetAttempts(String username, String ip) {
        redisTemplate.delete(REDIS_KEY_PREFIX + username);
        redisTemplate.delete(IP_REDIS_KEY_PREFIX + ip);
    }

    /**
     * 获取剩余锁定时间（分钟）
     */
    public long getLockTime(String username, String ip) {
        // 获取用户名锁定剩余时间
        String userKey = REDIS_KEY_PREFIX + username;
        Long userExpire = redisTemplate.getExpire(userKey, TimeUnit.MINUTES);
        if (userExpire != null && userExpire > 0) {
            return userExpire;
        }

        // 获取IP锁定剩余时间
        String ipKey = IP_REDIS_KEY_PREFIX + ip;
        Long ipExpire = redisTemplate.getExpire(ipKey, TimeUnit.MINUTES);
        return ipExpire != null ? ipExpire : 0;
    }

    /**
     * 检查是否需要锁定
     */
    private void checkLock(String username, int userAttempts, String ip, int ipAttempts) {
        // 检查用户名是否达到锁定阈值
        if (userAttempts >= MAX_ATTEMPTS) {
            eventPublisher.publish(new SecurityEvent(username, SecurityEvent.SecurityEventType.ACCOUNT_LOCKED) {});
        }

        // 检查IP是否达到锁定阈值
        if (ipAttempts >= IP_MAX_ATTEMPTS) {
            eventPublisher.publish(new SecurityEvent(ip, SecurityEvent.SecurityEventType.ACCOUNT_LOCKED) {});
        }
    }
} 