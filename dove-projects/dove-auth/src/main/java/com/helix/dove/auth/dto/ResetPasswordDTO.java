package com.helix.dove.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Reset Password DTO
 */
@Data
public class ResetPasswordDTO {

    /**
     * Email or Mobile
     */
    @NotBlank(message = "{user.account.not-blank}")
    private String account;

    /**
     * Account Type (email/mobile)
     */
    @NotBlank(message = "{user.account-type.not-blank}")
    private String accountType;

    /**
     * New Password
     */
    @NotBlank(message = "{user.password.not-blank}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "{user.password.pattern}")
    private String newPassword;

    /**
     * Confirm New Password
     */
    @NotBlank(message = "{user.confirm-password.not-blank}")
    private String confirmNewPassword;

    /**
     * Verification Code
     */
    @NotBlank(message = "{user.verification-code.not-blank}")
    private String verificationCode;

    /**
     * Verification Code Key
     */
    @NotBlank(message = "{user.verification-code-key.not-blank}")
    private String verificationCodeKey;

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
} 