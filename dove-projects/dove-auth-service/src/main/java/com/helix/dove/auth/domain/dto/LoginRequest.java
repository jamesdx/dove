package com.helix.dove.auth.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "{validation.username.notBlank}")
    private String username;

    @NotBlank(message = "{validation.password.notBlank}")
    private String password;

    private Boolean remember = false;
} 