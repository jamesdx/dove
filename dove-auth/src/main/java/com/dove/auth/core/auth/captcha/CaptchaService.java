package com.dove.auth.core.auth.captcha;

import com.dove.auth.core.security.SecurityEventPublisher;
import com.dove.auth.core.security.event.CaptchaErrorEvent;
import com.dove.common.core.exception.CaptchaException;
import com.google.code.kaptcha.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务
 */
@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final Producer captchaProducer;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SecurityEventPublisher eventPublisher;

    /**
     * 生成验证码
     */
    public CaptchaResult generateCaptcha() {
        // 生成验证码
        String captchaText = captchaProducer.createText();
        BufferedImage image = captchaProducer.createImage(captchaText);

        // 转换图片为base64
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (Exception e) {
            throw new RuntimeException("验证码图片生成失败", e);
        }

        // 生成验证码ID
        String captchaId = UUID.randomUUID().toString();

        // 将验证码保存到Redis
        String key = getCaptchaKey(captchaId);
        redisTemplate.opsForValue().set(key, captchaText, 5, TimeUnit.MINUTES);

        return new CaptchaResult(captchaId, os.toByteArray());
    }

    /**
     * 验证验证码
     */
    public void verify(String captchaId, String captchaCode) {
        String key = getCaptchaKey(captchaId);
        String correctCode = (String) redisTemplate.opsForValue().get(key);

        if (correctCode == null) {
            eventPublisher.publishEvent(new CaptchaErrorEvent(captchaId, "验证码已过期"));
            throw new CaptchaException("验证码已过期");
        }

        // 验证后删除验证码
        redisTemplate.delete(key);

        if (!correctCode.equalsIgnoreCase(captchaCode)) {
            eventPublisher.publishEvent(new CaptchaErrorEvent(captchaId, "验证码错误"));
            throw new CaptchaException("验证码错误");
        }
    }

    /**
     * 获取验证码key
     */
    private String getCaptchaKey(String captchaId) {
        return "dove:auth:captcha:" + captchaId;
    }
}

/**
 * 验证码结果
 */
@Data
@AllArgsConstructor
class CaptchaResult {
    /**
     * 验证码ID
     */
    private String captchaId;

    /**
     * 验证码图片（字节数组）
     */
    private byte[] imageBytes;
} 