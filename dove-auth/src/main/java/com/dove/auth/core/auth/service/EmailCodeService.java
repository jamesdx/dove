package com.dove.auth.core.auth.service;

import com.dove.auth.core.event.SecurityEventPublisher;
import com.dove.auth.core.event.SecurityEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 邮件验证码服务
 */
@Service
@RequiredArgsConstructor
public class EmailCodeService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender mailSender;
    private final SecurityEventPublisher eventPublisher;

    /**
     * Redis key前缀
     */
    private static final String REDIS_KEY_PREFIX = "dove:auth:email:code:";

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
    public void sendCode(String email) {
        // 生成验证码
        String code = generateCode();

        // 保存验证码
        String key = REDIS_KEY_PREFIX + email;
        redisTemplate.opsForValue().set(key, code, EXPIRE_TIME, TimeUnit.MINUTES);

        // 发送邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("验证码");
        message.setText("您的验证码是：" + code + "，" + EXPIRE_TIME + "分钟内有效。");
        mailSender.send(message);
    }

    /**
     * 验证验证码
     */
    public boolean verify(String email, String code) {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(code)) {
            return false;
        }

        // 获取验证码
        String key = REDIS_KEY_PREFIX + email;
        String savedCode = redisTemplate.opsForValue().get(key);

        // 验证码不存在或已过期
        if (StringUtils.isEmpty(savedCode)) {
            eventPublisher.publish(new SecurityEvent(email, SecurityEvent.SecurityEventType.CAPTCHA_ERROR) {});
            return false;
        }

        // 验证码不匹配
        if (!savedCode.equals(code)) {
            eventPublisher.publish(new SecurityEvent(email, SecurityEvent.SecurityEventType.CAPTCHA_ERROR) {});
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