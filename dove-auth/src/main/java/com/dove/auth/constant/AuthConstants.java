package com.dove.auth.constant;

/**
 * 认证常量
 */
public interface AuthConstants {

    /**
     * 认证请求头
     */
    String AUTHORIZATION_HEADER = "Authorization";

    /**
     * Bearer前缀
     */
    String BEARER_PREFIX = "Bearer ";

    /**
     * Basic前缀
     */
    String BASIC_PREFIX = "Basic ";

    /**
     * JWT存储前缀
     */
    String JWT_PREFIX = "jwt:";

    /**
     * 验证码存储前缀
     */
    String CAPTCHA_PREFIX = "captcha:";

    /**
     * 短信验证码存储前缀
     */
    String SMS_CODE_PREFIX = "sms:code:";

    /**
     * 邮箱验证码存储前缀
     */
    String EMAIL_CODE_PREFIX = "email:code:";

    /**
     * 登录失败次数存储前缀
     */
    String LOGIN_FAIL_PREFIX = "login:fail:";

    /**
     * IP封禁存储前缀
     */
    String IP_BLOCK_PREFIX = "ip:block:";

    /**
     * 用户Token存储前缀
     */
    String USER_TOKEN_PREFIX = "user:token:";

    /**
     * 在线用户存储前缀
     */
    String ONLINE_USER_PREFIX = "online:user:";

    /**
     * Remember Me Cookie名称
     */
    String REMEMBER_ME_COOKIE = "remember-me";

    /**
     * Remember Me过期时间(14天)
     */
    int REMEMBER_ME_EXPIRE = 14 * 24 * 60 * 60;

    /**
     * 登录失败最大次数
     */
    int MAX_LOGIN_FAIL_COUNT = 5;

    /**
     * 登录锁定时间(分钟)
     */
    int LOGIN_LOCK_TIME = 30;

    /**
     * IP封禁时间(小时)
     */
    int IP_BLOCK_TIME = 24;

    /**
     * Token过期时间(小时)
     */
    int TOKEN_EXPIRE_TIME = 2;

    /**
     * 刷新Token过期时间(天)
     */
    int REFRESH_TOKEN_EXPIRE_TIME = 7;
} 