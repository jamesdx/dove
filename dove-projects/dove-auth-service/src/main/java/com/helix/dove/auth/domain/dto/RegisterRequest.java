package com.helix.dove.auth.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "{validation.username.notBlank}")
    @Size(min = 4, max = 50, message = "{validation.username.size}")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "{validation.username.pattern}")
    private String username;

    @NotBlank(message = "{validation.email.notBlank}")
    @Email(message = "{validation.email.invalid}")
    private String email;

    @NotBlank(message = "{validation.password.notBlank}")
    @Size(min = 8, max = 100, message = "{validation.password.size}")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "{validation.password.pattern}"
    )
    private String password;

    @NotBlank(message = "{validation.confirmPassword.notBlank}")
    private String confirmPassword;

    private String locale;
    private String timezone;
    private Long tenantId;
} 