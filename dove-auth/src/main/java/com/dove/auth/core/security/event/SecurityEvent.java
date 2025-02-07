package com.dove.auth.core.security.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 安全事件基类
 */
@Getter
public abstract class SecurityEvent extends ApplicationEvent {
    
    /**
     * 事件时间戳
     */
    private final long timestamp;

    public SecurityEvent(Object source) {
        super(source);
        this.timestamp = System.currentTimeMillis();
    }
} 