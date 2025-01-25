package com.helix.dove.auth.controller;

import com.helix.dove.auth.dto.LoginRequest;
import com.helix.dove.auth.dto.LoginResponse;
import com.helix.dove.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "认证接口")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public void logout() {
        authService.logout();
    }

    @Operation(summary = "刷新Token")
    @PostMapping("/refresh")
    public LoginResponse refresh(@RequestHeader("Authorization") String token) {
        return authService.refresh(token);
    }
} 