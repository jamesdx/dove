package com.helix.dove.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * User Response DTO
 */
@Data
public class UserResponseDTO {

    /**
     * User ID
     */
    private Long id;

    /**
     * Username
     */
    private String username;

    /**
     * Email
     */
    private String email;

    /**
     * Status: 1-Normal, 2-Locked, 3-Disabled
     */
    private Integer status;

    /**
     * Account type: 1-Personal, 2-Enterprise
     */
    private Integer accountType;

    /**
     * Last login time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;

    /**
     * Last login IP
     */
    private String lastLoginIp;

    /**
     * Locale preference
     */
    private String locale;

    /**
     * Timezone
     */
    private String timezone;

    /**
     * Date format
     */
    private String dateFormat;

    /**
     * Time format
     */
    private String timeFormat;

    /**
     * Currency
     */
    private String currency;

    /**
     * Region
     */
    private String region;

    /**
     * Create time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * Update time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
} 