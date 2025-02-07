package com.dove.auth.controller.login;

import com.dove.auth.api.dto.request.LoginRequest;
import com.dove.auth.api.dto.response.LoginResponse;
import com.dove.common.web.response.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 登录控制器
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        // TODO: 实现登录逻辑
        return Result.success(new LoginResponse());
    }

    /**
     * 发送验证码
     */
    @PostMapping("/code/send")
    public Result<Void> sendCode(@RequestParam String type, @RequestParam String target) {
        // TODO: 实现发送验证码逻辑
        return Result.success();
    }

    /**
     * 验证验证码
     */
    @PostMapping("/code/verify")
    public Result<Void> verifyCode(@RequestParam String type, @RequestParam String target, @RequestParam String code) {
        // TODO: 实现验证码验证逻辑
        return Result.success();
    }
} 