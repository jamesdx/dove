package com.helix.dove.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "登录响应")
public class LoginResponse {

    @Schema(description = "访问令牌")
    private String accessToken;

    @Schema(description = "刷新令牌")
    private String refreshToken;

    @Schema(description = "过期时间(秒)")
    private Long expiresIn;

    @Schema(description = "用户设置")
    private UserSettings userSettings;

    @Data
    @Builder
    public static class UserSettings {
        @Schema(description = "语言")
        private String locale;

        @Schema(description = "时区")
        private String timezone;

        @Schema(description = "日期格式")
        private String dateFormat;

        @Schema(description = "时间格式")
        private String timeFormat;

        @Schema(description = "货币")
        private String currency;
    }
} 