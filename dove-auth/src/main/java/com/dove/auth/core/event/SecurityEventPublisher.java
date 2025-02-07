package com.dove.auth.core.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 安全事件发布器
 */
@Component
@RequiredArgsConstructor
public class SecurityEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 发布事件
     */
    public void publish(SecurityEvent event) {
        eventPublisher.publishEvent(event);
    }
} 