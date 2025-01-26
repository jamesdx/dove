package com.helix.dove.auth.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Register User DTO
 */
@Data
public class RegisterUserDTO {

    /**
     * Username
     */
    @NotBlank(message = "{user.username.not-blank}")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "{user.username.pattern}")
    private String username;

    /**
     * Password
     */
    @NotBlank(message = "{user.password.not-blank}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "{user.password.pattern}")
    private String password;

    /**
     * Confirm Password
     */
    @NotBlank(message = "{user.confirm-password.not-blank}")
    private String confirmPassword;

    /**
     * Email
     */
    @NotBlank(message = "{user.email.not-blank}")
    @Email(message = "{user.email.pattern}")
    private String email;

    /**
     * Mobile
     */
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "{user.mobile.pattern}")
    private String mobile;

    /**
     * Nickname
     */
    @Size(min = 2, max = 32, message = "{user.nickname.size}")
    private String nickname;

    /**
     * Verification Code
     */
    @NotBlank(message = "{user.verification-code.not-blank}")
    private String verificationCode;

    /**
     * Verification Code Type (email/sms)
     */
    @NotBlank(message = "{user.verification-code-type.not-blank}")
    private String verificationCodeType;

    /**
     * Captcha Code
     */
    @NotBlank(message = "{user.captcha.not-blank}")
    private String captchaCode;

    /**
     * Captcha Key
     */
    @NotBlank(message = "{user.captcha-key.not-blank}")
    private String captchaKey;

    /**
     * Tenant Code
     */
    private String tenantCode;

    /**
     * Accept Terms
     */
    @AssertTrue(message = "{user.accept-terms.true}")
    private Boolean acceptTerms;
} 