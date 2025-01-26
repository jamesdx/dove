package com.helix.dove.auth.controller;

import com.helix.dove.auth.dto.LoginDTO;
import com.helix.dove.auth.dto.LoginResultDTO;
import com.helix.dove.auth.service.UserService;
import com.helix.dove.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Login Controller
 */
@Tag(name = "Login API", description = "Login related APIs")
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @Operation(summary = "User login")
    @PostMapping
    public CommonResult<LoginResultDTO> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        // Set login IP
        loginDTO.setLoginIp(getClientIp(request));
        // Set device info
        loginDTO.setDeviceInfo(request.getHeader("User-Agent"));
        
        LoginResultDTO result = userService.login(loginDTO);
        return CommonResult.success(result);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
} 