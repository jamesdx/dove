package com.helix.dove.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "登录请求")
public class LoginRequest {

    @Schema(description = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "记住我")
    private Boolean remember = false;

    @Schema(description = "语言")
    private String locale;

    @Schema(description = "时区")
    private String timezone;
} 