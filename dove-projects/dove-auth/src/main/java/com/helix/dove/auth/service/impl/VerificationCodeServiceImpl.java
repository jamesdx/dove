package com.helix.dove.auth.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.RandomUtil;
import com.helix.dove.auth.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Verification Code Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String EMAIL_CODE_PREFIX = "email:code:";
    private static final String SMS_CODE_PREFIX = "sms:code:";
    private static final String CAPTCHA_CODE_PREFIX = "captcha:code:";

    private static final long CODE_EXPIRE_TIME = 5;
    private static final int CODE_LENGTH = 6;

    @Override
    public String sendEmailCode(String email) {
        String code = generateCode();
        String key = EMAIL_CODE_PREFIX + email;
        // TODO: Send email with verification code
        log.info("Send email verification code: {} to {}", code, email);
        // Save code to Redis
        redisTemplate.opsForValue().set(key, code, CODE_EXPIRE_TIME, TimeUnit.MINUTES);
        return key;
    }

    @Override
    public String sendSmsCode(String mobile) {
        String code = generateCode();
        String key = SMS_CODE_PREFIX + mobile;
        // TODO: Send SMS with verification code
        log.info("Send SMS verification code: {} to {}", code, mobile);
        // Save code to Redis
        redisTemplate.opsForValue().set(key, code, CODE_EXPIRE_TIME, TimeUnit.MINUTES);
        return key;
    }

    @Override
    public CaptchaResult generateCaptcha() {
        // Generate captcha
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(200, 100);
        String code = captcha.getCode();
        String key = CAPTCHA_CODE_PREFIX + RandomUtil.randomString(16);
        // Save code to Redis
        redisTemplate.opsForValue().set(key, code, CODE_EXPIRE_TIME, TimeUnit.MINUTES);
        return new CaptchaResult(key, captcha.getImageBase64());
    }

    private String generateCode() {
        return RandomUtil.randomNumbers(CODE_LENGTH);
    }
} 