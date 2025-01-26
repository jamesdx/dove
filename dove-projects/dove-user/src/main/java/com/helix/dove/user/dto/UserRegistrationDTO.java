package com.helix.dove.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * User Registration DTO
 */
@Data
public class UserRegistrationDTO {

    /**
     * Username
     */
    @NotBlank(message = "{user.username.notBlank}")
    @Size(min = 4, max = 32, message = "{user.username.size}")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "{user.username.pattern}")
    private String username;

    /**
     * Email
     */
    @NotBlank(message = "{user.email.notBlank}")
    @Email(message = "{user.email.format}")
    private String email;

    /**
     * Password
     */
    @NotBlank(message = "{user.password.notBlank}")
    @Size(min = 8, max = 32, message = "{user.password.size}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "{user.password.pattern}")
    private String password;

    /**
     * Account type: 1-Personal, 2-Enterprise
     */
    private Integer accountType = 1;

    /**
     * Locale preference
     */
    @Pattern(regexp = "^[a-z]{2}_[A-Z]{2}$", message = "{user.locale.pattern}")
    private String locale;

    /**
     * Timezone
     */
    private String timezone;

    /**
     * Region
     */
    @Pattern(regexp = "^[A-Z]{2}$", message = "{user.region.pattern}")
    private String region;

    /**
     * Tenant ID
     */
    @NotBlank(message = "{user.tenantId.notBlank}")
    private String tenantId;
} 