package com.dove.auth.core.session;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 会话配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "dove.session")
public class SessionProperties {

    /**
     * 会话超时时间（分钟）
     */
    private int timeout = 30;

    /**
     * 最大会话数
     */
    private int maximumSessions = 1;

    /**
     * 达到最大会话数时是否阻止新的登录
     */
    private boolean maxSessionsPreventsLogin = false;

    /**
     * 是否允许同一账号并发登录
     */
    private boolean concurrentLoginPermitted = true;

    /**
     * 记住我功能过期时间（天）
     */
    private int rememberMeTimeout = 7;

    /**
     * 会话清理间隔（分钟）
     */
    private int cleanupInterval = 5;

    /**
     * 会话监控间隔（分钟）
     */
    private int monitorInterval = 1;

    /**
     * Redis缓存前缀
     */
    private String redisKeyPrefix = "dove:auth:session:";

    /**
     * 排除的URL列表
     */
    private String[] excludeUrls = {
            "/auth/login",
            "/auth/logout",
            "/auth/captcha",
            "/auth/sms/code",
            "/auth/email/code",
            "/auth/oauth2/**",
            "/actuator/**"
    };

    /**
     * 是否启用会话监控
     */
    private boolean monitorEnabled = true;

    /**
     * 是否启用会话持久化
     */
    private boolean persistenceEnabled = true;

    /**
     * 是否启用会话验证
     */
    private boolean validationEnabled = true;

    /**
     * 会话验证间隔（分钟）
     */
    private int validationInterval = 1;

    /**
     * 是否启用会话集群
     */
    private boolean clusterEnabled = true;

    /**
     * 集群同步间隔（毫秒）
     */
    private int clusterSyncInterval = 1000;

    /**
     * 是否记录会话访问日志
     */
    private boolean accessLogEnabled = true;

    /**
     * 是否记录会话操作日志
     */
    private boolean operationLogEnabled = true;
} 