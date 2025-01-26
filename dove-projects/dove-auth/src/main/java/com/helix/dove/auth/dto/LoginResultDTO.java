package com.helix.dove.auth.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Login Result DTO
 */
@Data
@Builder
public class LoginResultDTO {

    /**
     * Access Token
     */
    private String accessToken;

    /**
     * Token Type
     */
    private String tokenType;

    /**
     * Refresh Token
     */
    private String refreshToken;

    /**
     * Expires In (seconds)
     */
    private Long expiresIn;

    /**
     * User ID
     */
    private Long userId;

    /**
     * Username
     */
    private String username;

    /**
     * Nickname
     */
    private String nickname;

    /**
     * Avatar
     */
    private String avatar;

    /**
     * Tenant Code
     */
    private String tenantCode;

    /**
     * Roles
     */
    private String[] roles;

    /**
     * Permissions
     */
    private String[] permissions;
} 