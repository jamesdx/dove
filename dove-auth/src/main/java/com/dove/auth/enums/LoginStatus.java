package com.dove.auth.enums;

import lombok.Getter;

/**
 * 登录状态枚举
 */
@Getter
public enum LoginStatus {

    SUCCESS("SUCCESS", "登录成功"),
    FAILURE("FAILURE", "登录失败"),
    LOCKED("LOCKED", "账号锁定"),
    DISABLED("DISABLED", "账号禁用"),
    EXPIRED("EXPIRED", "账号过期"),
    PASSWORD_EXPIRED("PASSWORD_EXPIRED", "密码过期"),
    TOO_MANY_ATTEMPTS("TOO_MANY_ATTEMPTS", "尝试次数过多"),
    CAPTCHA_ERROR("CAPTCHA_ERROR", "验证码错误"),
    IP_BLOCKED("IP_BLOCKED", "IP已封禁");

    private final String code;
    private final String desc;

    LoginStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static LoginStatus getByCode(String code) {
        for (LoginStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
} 