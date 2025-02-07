package com.dove.auth.enums;

import lombok.Getter;

/**
 * 登录类型枚举
 */
@Getter
public enum LoginType {

    USERNAME("USERNAME", "用户名登录"),
    EMAIL("EMAIL", "邮箱登录"),
    PHONE("PHONE", "手机号登录"),
    WECHAT("WECHAT", "微信登录"),
    DINGTALK("DINGTALK", "钉钉登录");

    private final String code;
    private final String desc;

    LoginType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static LoginType getByCode(String code) {
        for (LoginType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
} 