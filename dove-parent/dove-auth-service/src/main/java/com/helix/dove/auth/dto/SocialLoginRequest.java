package com.helix.dove.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "社交登录请求")
public class SocialLoginRequest {

    @Schema(description = "社交平台访问令牌")
    @NotBlank(message = "访问令牌不能为空")
    private String accessToken;

    @Schema(description = "社交平台ID令牌（仅用于部分平台）")
    private String idToken;

    @Schema(description = "语言")
    private String locale;

    @Schema(description = "时区")
    private String timezone;

    @Schema(description = "记住我")
    private Boolean remember = false;
} 