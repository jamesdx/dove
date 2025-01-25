package com.helix.dove.auth.controller;

import com.helix.dove.auth.domain.dto.LoginRequest;
import com.helix.dove.auth.domain.dto.LoginResponse;
import com.helix.dove.auth.domain.dto.RegisterRequest;
import com.helix.dove.auth.domain.dto.RegisterResponse;
import com.helix.dove.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证接口", description = "用户认证相关接口")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @Operation(summary = "检查用户名是否可用")
    @GetMapping("/check-username")
    public boolean checkUsername(@RequestParam String username) {
        return userService.isUsernameAvailable(username);
    }

    @Operation(summary = "检查邮箱是否可用")
    @GetMapping("/check-email")
    public boolean checkEmail(@RequestParam String email) {
        return userService.isEmailAvailable(email);
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/user-info")
    public LoginResponse getUserInfo() {
        // TODO: 实现获取当前用户信息的逻辑
        return null;
    }

    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public void logout() {
        // TODO: 实现用户登出的逻辑
    }
} 