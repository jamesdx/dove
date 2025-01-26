package com.helix.dove.auth.controller;

import com.helix.dove.auth.service.VerificationCodeService;
import com.helix.dove.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Verification Code Controller
 */
@Tag(name = "Verification Code API", description = "Verification code related APIs")
@Validated
@RestController
@RequestMapping("/verification-code")
@RequiredArgsConstructor
public class VerificationCodeController {

    private final VerificationCodeService verificationCodeService;

    @Operation(summary = "Send email verification code")
    @PostMapping("/email")
    public CommonResult<Map<String, String>> sendEmailCode(@RequestParam @Email String email) {
        String key = verificationCodeService.sendEmailCode(email);
        Map<String, String> result = new HashMap<>();
        result.put("key", key);
        return CommonResult.success(result);
    }

    @Operation(summary = "Send SMS verification code")
    @PostMapping("/sms")
    public CommonResult<Map<String, String>> sendSmsCode(@RequestParam @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$") String mobile) {
        String key = verificationCodeService.sendSmsCode(mobile);
        Map<String, String> result = new HashMap<>();
        result.put("key", key);
        return CommonResult.success(result);
    }

    @Operation(summary = "Generate captcha code")
    @GetMapping("/captcha")
    public CommonResult<Map<String, String>> generateCaptcha() {
        VerificationCodeService.CaptchaResult captcha = verificationCodeService.generateCaptcha();
        Map<String, String> result = new HashMap<>();
        result.put("key", captcha.getKey());
        result.put("image", captcha.getImage());
        return CommonResult.success(result);
    }
} 