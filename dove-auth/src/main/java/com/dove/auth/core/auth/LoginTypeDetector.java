package com.dove.auth.core.auth;

import com.dove.common.core.enums.LoginType;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 登录类型检测器
 */
@Component
public class LoginTypeDetector {

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^1[3-9]\\d{9}$");

    /**
     * 检测登录类型
     *
     * @param identifier 登录标识符
     * @return 登录类型
     */
    public LoginType detectLoginType(String identifier) {
        if (identifier == null) {
            throw new IllegalArgumentException("登录标识符不能为空");
        }

        if (EMAIL_PATTERN.matcher(identifier).matches()) {
            return LoginType.EMAIL;
        }

        if (PHONE_PATTERN.matcher(identifier).matches()) {
            return LoginType.PHONE;
        }

        return LoginType.USERNAME;
    }

    /**
     * 验证登录标识符格式
     *
     * @param identifier 登录标识符
     * @param loginType 登录类型
     * @return 是否有效
     */
    public boolean validateIdentifier(String identifier, LoginType loginType) {
        if (identifier == null || loginType == null) {
            return false;
        }

        return switch (loginType) {
            case EMAIL -> EMAIL_PATTERN.matcher(identifier).matches();
            case PHONE -> PHONE_PATTERN.matcher(identifier).matches();
            case USERNAME -> identifier.length() >= 3 && identifier.length() <= 20;
            default -> false;
        };
    }
} 