package com.dove.auth.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求DTO
 */
@Data
public class LoginRequest {

    /**
     * 登录标识(用户名/邮箱/手机号)
     */
    @NotBlank(message = "{login.identifier.notBlank}")
    private String identifier;

    /**
     * 密码
     */
    @NotBlank(message = "{login.password.notBlank}")
    private String password;

    /**
     * 验证码
     */
    private String captcha;

    /**
     * 验证码ID
     */
    private String captchaId;

    /**
     * 记住我
     */
    private Boolean rememberMe;

    /**
     * 双因素认证码
     */
    private String twoFactorCode;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 设备类型
     */
    private String deviceType;
} 