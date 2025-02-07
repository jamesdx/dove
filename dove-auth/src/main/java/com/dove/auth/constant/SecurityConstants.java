package com.dove.auth.constant;

/**
 * 安全相关常量
 */
public interface SecurityConstants {

    /**
     * 权限相关常量
     */
    String ROLE_PREFIX = "ROLE_";
    String ADMIN_ROLE = "ROLE_ADMIN";
    String USER_ROLE = "ROLE_USER";
    String GUEST_ROLE = "ROLE_GUEST";
    
    /**
     * 权限分隔符
     */
    String PERMISSION_DELIMITER = ":";
    
    /**
     * 资源服务器相关常量
     */
    String RESOURCE_SERVER_CONFIGURER = "resourceServerConfigurerAdapter";
    String RESOURCE_ID = "dove-resource";
    
    /**
     * 安全配置相关常量
     */
    String[] IGNORE_URLS = {
        "/actuator/**",
        "/auth/login",
        "/auth/logout",
        "/auth/captcha",
        "/auth/register",
        "/auth/forget-password",
        "/auth/reset-password",
        "/auth/oauth2/**",
        "/doc.html",
        "/webjars/**",
        "/swagger-resources/**",
        "/v3/api-docs/**"
    };
    
    /**
     * 密码策略相关常量
     */
    int PASSWORD_MIN_LENGTH = 8;
    int PASSWORD_MAX_LENGTH = 20;
    String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$";
    int PASSWORD_HISTORY_SIZE = 5;
    int PASSWORD_EXPIRE_DAYS = 90;
    
    /**
     * 安全审计相关常量
     */
    String AUDIT_SUCCESS = "SUCCESS";
    String AUDIT_FAILURE = "FAILURE";
    String AUDIT_TYPE_LOGIN = "LOGIN";
    String AUDIT_TYPE_LOGOUT = "LOGOUT";
    String AUDIT_TYPE_PASSWORD_CHANGE = "PASSWORD_CHANGE";
    String AUDIT_TYPE_PERMISSION_CHANGE = "PERMISSION_CHANGE";
    
    /**
     * 会话相关常量
     */
    int MAX_SESSIONS_PER_USER = 5;
    boolean PREVENT_LOGIN = true;
    
    /**
     * 安全防护相关常量
     */
    int XSS_LENGTH = 65536;
    String[] XSS_EXCLUDE_URLS = {
        "/auth/login",
        "/auth/oauth2/**"
    };
    
    /**
     * CORS配置相关常量
     */
    String[] ALLOWED_ORIGINS = {"*"};
    String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
    String[] ALLOWED_HEADERS = {"*"};
    long MAX_AGE = 3600;
    
    /**
     * 加密相关常量
     */
    int BCRYPT_STRENGTH = 10;
    String AES_KEY = "your-aes-key-here";
    String RSA_PUBLIC_KEY = "your-rsa-public-key";
    String RSA_PRIVATE_KEY = "your-rsa-private-key";

    /**
     * 用户名正则
     * 4-20位字母、数字、下划线
     */
    String USERNAME_PATTERN = "^[a-zA-Z0-9_]{4,20}$";

    /**
     * 手机号正则
     */
    String MOBILE_PATTERN = "^1[3-9]\\d{9}$";

    /**
     * 邮箱正则
     */
    String EMAIL_PATTERN = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";

    /**
     * 安全会话属性名
     */
    String SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    /**
     * 验证码长度
     */
    int CAPTCHA_LENGTH = 6;

    /**
     * 验证码有效期(分钟)
     */
    int CAPTCHA_EXPIRE = 5;

    /**
     * 同一IP每日发送验证码最大次数
     */
    int MAX_DAILY_CAPTCHA = 10;

    /**
     * 同一手机号每日发送验证码最大次数
     */
    int MAX_DAILY_MOBILE_CAPTCHA = 5;

    /**
     * 同一邮箱每日发送验证码最大次数
     */
    int MAX_DAILY_EMAIL_CAPTCHA = 5;

    /**
     * 异地登录检测距离阈值(公里)
     */
    int LOGIN_DISTANCE_THRESHOLD = 100;
} 