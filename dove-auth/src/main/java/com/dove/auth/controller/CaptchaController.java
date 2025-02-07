package com.dove.auth.controller;

import com.dove.auth.core.auth.captcha.CaptchaService;
import com.dove.common.core.model.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码控制器
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaService captchaService;

    /**
     * 生成验证码
     */
    @GetMapping("/captcha")
    public Result<Map<String, String>> generateCaptcha() {
        CaptchaResult captcha = captchaService.generateCaptcha();
        
        Map<String, String> result = new HashMap<>();
        result.put("captchaId", captcha.getCaptchaId());
        result.put("imageBase64", Base64.getEncoder().encodeToString(captcha.getImageBytes()));
        
        return Result.success(result);
    }
} 