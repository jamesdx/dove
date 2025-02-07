package com.dove.auth.core.security;

import com.dove.auth.core.event.SecurityEventPublisher;
import com.dove.auth.core.event.SecurityEvent;
import com.dove.common.core.model.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 异常登录检测
 */
@Component
@RequiredArgsConstructor
public class AbnormalLoginDetector {

    private final RedisTemplate<String, Object> redisTemplate;
    private final SecurityEventPublisher eventPublisher;

    /**
     * Redis key前缀
     */
    private static final String REDIS_KEY_PREFIX = "dove:auth:login:device:";

    /**
     * 设备信息过期时间（天）
     */
    private static final int EXPIRE_TIME = 90;

    /**
     * 检测异常登录
     */
    public void detect(LoginUser loginUser, String ip, String userAgent, String deviceId) {
        String key = REDIS_KEY_PREFIX + loginUser.getUserId();

        // 获取上次登录信息
        LoginInfo lastLoginInfo = (LoginInfo) redisTemplate.opsForValue().get(key);

        // 检查是否是新设备登录
        if (lastLoginInfo != null && !lastLoginInfo.getDeviceId().equals(deviceId)) {
            // 发布异常登录事件
            eventPublisher.publish(new SecurityEvent(loginUser, SecurityEvent.SecurityEventType.ABNORMAL_LOGIN) {});
        }

        // 更新登录信息
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUserId(loginUser.getUserId());
        loginInfo.setUsername(loginUser.getUsername());
        loginInfo.setIp(ip);
        loginInfo.setUserAgent(userAgent);
        loginInfo.setDeviceId(deviceId);
        loginInfo.setLoginTime(System.currentTimeMillis());

        redisTemplate.opsForValue().set(key, loginInfo, EXPIRE_TIME, TimeUnit.DAYS);
    }

    /**
     * 登录信息
     */
    private static class LoginInfo {
        private Long userId;
        private String username;
        private String ip;
        private String userAgent;
        private String deviceId;
        private Long loginTime;

        // Getters and setters
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getUserAgent() {
            return userAgent;
        }

        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public Long getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(Long loginTime) {
            this.loginTime = loginTime;
        }
    }
} 