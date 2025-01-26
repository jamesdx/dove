package com.helix.dove.auth.controller;

import com.helix.dove.auth.dto.ResetPasswordDTO;
import com.helix.dove.auth.service.UserService;
import com.helix.dove.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Password Controller
 */
@Tag(name = "Password API", description = "Password management related APIs")
@RestController
@RequestMapping("/password")
@RequiredArgsConstructor
public class PasswordController {

    private final UserService userService;

    @Operation(summary = "Reset password")
    @PostMapping("/reset")
    public CommonResult<Void> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        userService.resetPassword(resetPasswordDTO);
        return CommonResult.success(null);
    }
} 