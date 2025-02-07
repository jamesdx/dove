package com.dove.auth.core.constants;

/**
 * 认证常量
 */
public class AuthConstants {
    
    /**
     * Token相关常量
     */
    public static class Token {
        /** Token前缀 */
        public static final String PREFIX = "Bearer ";
        /** Token请求头 */
        public static final String HEADER = "Authorization";
        /** Token参数名 */
        public static final String PARAM_NAME = "token";
        /** Token刷新参数名 */
        public static final String REFRESH_PARAM = "refreshToken";
    }

    /**
     * Redis缓存相关常量
     */
    public static class Cache {
        /** Token缓存前缀 */
        public static final String TOKEN_PREFIX = "dove:auth:token:";
        /** 刷新Token缓存前缀 */
        public static final String REFRESH_TOKEN_PREFIX = "dove:auth:refresh_token:";
        /** 验证码缓存前缀 */
        public static final String CAPTCHA_PREFIX = "dove:auth:captcha:";
        /** 用户Token关联前缀 */
        public static final String USER_TOKEN_PREFIX = "dove:auth:user_token:";
    }

    /**
     * 登录相关常量
     */
    public static class Login {
        /** 最大重试次数 */
        public static final int MAX_RETRY_COUNT = 5;
        /** 锁定时间（分钟） */
        public static final int LOCK_TIME = 10;
        /** 验证码有效期（分钟） */
        public static final int CAPTCHA_EXPIRE = 5;
        /** 记住我过期时间（天） */
        public static final int REMEMBER_ME_EXPIRE = 7;
    }

    /**
     * 安全相关常量
     */
    public static class Security {
        /** 超级管理员角色编码 */
        public static final String ADMIN_ROLE = "ROLE_ADMIN";
        /** 默认角色编码 */
        public static final String DEFAULT_ROLE = "ROLE_USER";
        /** 匿名用户角色编码 */
        public static final String ANONYMOUS_ROLE = "ROLE_ANONYMOUS";
    }

    private AuthConstants() {
        throw new IllegalStateException("Constant class");
    }
} 