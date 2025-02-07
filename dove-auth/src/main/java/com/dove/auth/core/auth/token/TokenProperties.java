package com.dove.auth.core.auth.token;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Token配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "dove.token")
public class TokenProperties {

    /**
     * 令牌自定义标识
     */
    private String header = "Authorization";

    /**
     * 令牌前缀
     */
    private String prefix = "Bearer ";

    /**
     * 令牌密钥
     */
    private String secret = "abcdefghijklmnopqrstuvwxyz";

    /**
     * 令牌有效期（默认30分钟）
     */
    private int expireTime = 30;

    /**
     * 刷新令牌有效期（默认7天）
     */
    private int refreshExpireTime = 7;

    /**
     * 令牌续期阈值（默认5分钟）
     */
    private int refreshThreshold = 5;

    /**
     * 是否允许多端同时登录
     */
    private boolean multiLogin = true;

    /**
     * 最大登录用户数
     */
    private int maxLoginCount = 5;

    /**
     * 是否允许刷新令牌
     */
    private boolean allowRefresh = true;

    /**
     * 是否记住我
     */
    private boolean rememberMe = true;

    /**
     * 记住我有效期（默认7天）
     */
    private int rememberMeExpireTime = 7;

    /**
     * 令牌存储策略（redis/jwt）
     */
    private String storeStrategy = "redis";

    /**
     * Redis缓存前缀
     */
    private String redisKeyPrefix = "dove:auth:token:";

    /**
     * Redis缓存刷新令牌前缀
     */
    private String redisRefreshKeyPrefix = "dove:auth:refresh_token:";
} 