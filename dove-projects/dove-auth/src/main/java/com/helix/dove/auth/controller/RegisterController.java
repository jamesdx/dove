package com.helix.dove.auth.controller;

import com.helix.dove.auth.dto.RegisterUserDTO;
import com.helix.dove.auth.entity.User;
import com.helix.dove.auth.service.UserService;
import com.helix.dove.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Register Controller
 */
@Tag(name = "Register API", description = "User registration related APIs")
@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    @Operation(summary = "Register user")
    @PostMapping
    public CommonResult<User> register(@Valid @RequestBody RegisterUserDTO registerUserDTO) {
        User user = userService.register(registerUserDTO);
        return CommonResult.success(user);
    }

    @Operation(summary = "Check username exists")
    @GetMapping("/check-username")
    public CommonResult<Boolean> checkUsername(@RequestParam String username) {
        boolean exists = userService.checkUsernameExists(username);
        return CommonResult.success(exists);
    }

    @Operation(summary = "Check email exists")
    @GetMapping("/check-email")
    public CommonResult<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.checkEmailExists(email);
        return CommonResult.success(exists);
    }

    @Operation(summary = "Check mobile exists")
    @GetMapping("/check-mobile")
    public CommonResult<Boolean> checkMobile(@RequestParam String mobile) {
        boolean exists = userService.checkMobileExists(mobile);
        return CommonResult.success(exists);
    }
} 