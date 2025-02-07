package com.dove.auth.config.oauth2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * OAuth2配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "oauth2")
public class OAuth2Properties {

    /**
     * 客户端配置列表
     */
    private List<Client> clients = new ArrayList<>();

    /**
     * JWT签名密钥
     */
    private String signingKey;

    /**
     * 访问令牌有效期(秒)
     */
    private int accessTokenValidity = 7200;

    /**
     * 刷新令牌有效期(秒)
     */
    private int refreshTokenValidity = 604800;

    /**
     * 是否重用刷新令牌
     */
    private boolean reuseRefreshToken = true;

    /**
     * 是否支持刷新令牌
     */
    private boolean supportRefreshToken = true;

    /**
     * 客户端配置
     */
    @Data
    public static class Client {
        /**
         * 客户端ID
         */
        private String clientId;

        /**
         * 客户端密钥
         */
        private String clientSecret;

        /**
         * 授权类型
         */
        private List<String> authorizedGrantTypes = new ArrayList<>();

        /**
         * 授权范围
         */
        private List<String> scopes = new ArrayList<>();

        /**
         * 重定向URI
         */
        private List<String> redirectUris = new ArrayList<>();

        /**
         * 访问令牌有效期(秒)
         */
        private Integer accessTokenValidity;

        /**
         * 刷新令牌有效期(秒)
         */
        private Integer refreshTokenValidity;

        /**
         * 自动授权范围
         */
        private List<String> autoApproveScopes = new ArrayList<>();

        /**
         * 资源ID列表
         */
        private List<String> resourceIds = new ArrayList<>();

        /**
         * 是否需要授权码
         */
        private boolean requireAuthorizationCode = true;

        /**
         * 是否需要客户端密钥
         */
        private boolean requireClientSecret = true;
    }
} 