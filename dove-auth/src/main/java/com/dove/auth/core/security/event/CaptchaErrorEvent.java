package com.dove.auth.core.security.event;

import lombok.Getter;

/**
 * 验证码错误事件
 */
@Getter
public class CaptchaErrorEvent extends SecurityEvent {
    
    private final String captchaId;
    private final String reason;

    public CaptchaErrorEvent(String captchaId, String reason) {
        super(captchaId);
        this.captchaId = captchaId;
        this.reason = reason;
    }
} 