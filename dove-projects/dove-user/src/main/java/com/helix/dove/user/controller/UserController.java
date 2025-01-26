package com.helix.dove.user.controller;

import com.helix.dove.common.api.Result;
import com.helix.dove.user.converter.UserConverter;
import com.helix.dove.user.dto.UserRegistrationDTO;
import com.helix.dove.user.dto.UserResponseDTO;
import com.helix.dove.user.entity.User;
import com.helix.dove.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * User Controller
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Register user
     *
     * @param dto registration info
     * @return user info
     */
    @PostMapping("/register")
    public Result<UserResponseDTO> register(@Valid @RequestBody UserRegistrationDTO dto) {
        User user = UserConverter.INSTANCE.toUser(dto);
        user = userService.createUser(user);
        return Result.success(UserConverter.INSTANCE.toUserResponseDTO(user));
    }

    /**
     * Get user by ID
     *
     * @param id user ID
     * @param tenantId tenant ID
     * @return user info
     */
    @GetMapping("/{id}")
    public Result<UserResponseDTO> getUser(@PathVariable Long id,
            @RequestHeader("X-Tenant-ID") String tenantId) {
        User user = userService.getById(id);
        return Result.success(UserConverter.INSTANCE.toUserResponseDTO(user));
    }

    /**
     * Update user locale settings
     *
     * @param id user ID
     * @param locale locale
     * @param timezone timezone
     * @param dateFormat date format
     * @param timeFormat time format
     * @param currency currency
     * @param region region
     * @param tenantId tenant ID
     * @return updated user info
     */
    @PutMapping("/{id}/locale-settings")
    public Result<UserResponseDTO> updateLocaleSettings(
            @PathVariable Long id,
            @RequestParam String locale,
            @RequestParam String timezone,
            @RequestParam String dateFormat,
            @RequestParam String timeFormat,
            @RequestParam String currency,
            @RequestParam String region,
            @RequestHeader("X-Tenant-ID") String tenantId) {
        User user = userService.updateLocaleSettings(id, locale, timezone, dateFormat,
                timeFormat, currency, region, tenantId);
        return Result.success(UserConverter.INSTANCE.toUserResponseDTO(user));
    }

    /**
     * Update user status
     *
     * @param id user ID
     * @param status status
     * @param tenantId tenant ID
     * @return updated user info
     */
    @PutMapping("/{id}/status")
    public Result<UserResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status,
            @RequestHeader("X-Tenant-ID") String tenantId) {
        User user = userService.updateStatus(id, status, tenantId);
        return Result.success(UserConverter.INSTANCE.toUserResponseDTO(user));
    }
} 