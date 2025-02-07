package com.dove.auth.core.auth.service;

import com.dove.auth.core.event.SecurityEventPublisher;
import com.dove.auth.core.event.SecurityEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 短信验证码服务
 */
@Service
@RequiredArgsConstructor
public class SmsCodeService {

    private final RedisTemplate<String, String> redisTemplate;
    private final SecurityEventPublisher eventPublisher;

    /**
     * Redis key前缀
     */
    private static final String REDIS_KEY_PREFIX = "dove:auth:sms:code:";

    /**
     * 验证码有效期（分钟）
     */
    private static final int EXPIRE_TIME = 5;

    /**
     * 验证码长度
     */
    private static final int CODE_LENGTH = 6;

    /**
     * 发送验证码
     */
    public void sendCode(String mobile) {
        // 生成验证码
        String code = generateCode();

        // 保存验证码
        String key = REDIS_KEY_PREFIX + mobile;
        redisTemplate.opsForValue().set(key, code, EXPIRE_TIME, TimeUnit.MINUTES);

        // TODO: 调用短信服务发送验证码
        System.out.println("Send SMS code: " + code + " to mobile: " + mobile);
    }

    /**
     * 验证验证码
     */
    public boolean verify(String mobile, String code) {
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(code)) {
            return false;
        }

        // 获取验证码
        String key = REDIS_KEY_PREFIX + mobile;
        String savedCode = redisTemplate.opsForValue().get(key);

        // 验证码不存在或已过期
        if (StringUtils.isEmpty(savedCode)) {
            eventPublisher.publish(new SecurityEvent(mobile, SecurityEvent.SecurityEventType.CAPTCHA_ERROR) {});
            return false;
        }

        // 验证码不匹配
        if (!savedCode.equals(code)) {
            eventPublisher.publish(new SecurityEvent(mobile, SecurityEvent.SecurityEventType.CAPTCHA_ERROR) {});
            return false;
        }

        // 验证通过后删除验证码
        redisTemplate.delete(key);
        return true;
    }

    /**
     * 生成验证码
     */
    private String generateCode() {
        return RandomStringUtils.randomNumeric(CODE_LENGTH);
    }
} 