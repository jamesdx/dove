package com.dove.auth.config.oauth2;

import com.dove.auth.core.auth.provider.ThirdPartyAuthenticationProvider;
import com.dove.auth.core.auth.provider.TwoFactorAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * OAuth2主配置
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    private final OAuth2Properties oauth2Properties;
    private final AuthenticationManager authenticationManager;
    private final TwoFactorAuthenticationProvider twoFactorAuthenticationProvider;
    private final ThirdPartyAuthenticationProvider thirdPartyAuthenticationProvider;

    /**
     * 配置客户端详情
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置客户端
        var builder = clients.inMemory();
        oauth2Properties.getClients().forEach(client -> {
            try {
                builder.withClient(client.getClientId())
                        .secret(client.getClientSecret())
                        .authorizedGrantTypes(client.getAuthorizedGrantTypes().toArray(new String[0]))
                        .scopes(client.getScopes().toArray(new String[0]))
                        .redirectUris(client.getRedirectUris().toArray(new String[0]))
                        .accessTokenValiditySeconds(client.getAccessTokenValidity())
                        .refreshTokenValiditySeconds(client.getRefreshTokenValidity())
                        .autoApprove(client.getAutoApproveScopes().toArray(new String[0]))
                        .resourceIds(client.getResourceIds().toArray(new String[0]));
            } catch (Exception e) {
                throw new RuntimeException("Failed to configure client: " + client.getClientId(), e);
            }
        });
    }

    /**
     * 配置授权服务器端点
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter())
                .authenticationManager(authenticationManager)
                .reuseRefreshTokens(oauth2Properties.isReuseRefreshToken());
    }

    /**
     * 配置授权服务器安全
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }

    /**
     * 配置认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
                .authenticationProvider(twoFactorAuthenticationProvider)
                .authenticationProvider(thirdPartyAuthenticationProvider);
        return builder.build();
    }

    /**
     * 配置令牌存储
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * 配置JWT令牌转换器
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(oauth2Properties.getSigningKey());
        return converter;
    }
} 