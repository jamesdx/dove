package com.helix.dove.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Login DTO
 */
@Data
public class LoginDTO {

    /**
     * Username/Email/Mobile
     */
    @NotBlank(message = "{user.account.not-blank}")
    private String account;

    /**
     * Account Type (username/email/mobile)
     */
    @NotBlank(message = "{user.account-type.not-blank}")
    private String accountType;

    /**
     * Password
     */
    @NotBlank(message = "{user.password.not-blank}")
    private String password;

    /**
     * Tenant Code
     */
    private String tenantCode;

    /**
     * Remember Me
     */
    private Boolean rememberMe;

    /**
     * Captcha Code (required if login failed times >= 3)
     */
    private String captchaCode;

    /**
     * Captcha Key
     */
    private String captchaKey;

    /**
     * Device Info
     */
    private String deviceInfo;

    /**
     * Login IP
     */
    private String loginIp;
} 