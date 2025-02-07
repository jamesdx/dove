package com.dove.auth.config.oauth2;

import com.dove.auth.core.auth.provider.ThirdPartyAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;

/**
 * OAuth2登录配置器
 */
@Configuration
@RequiredArgsConstructor
public class OAuth2LoginConfigurer extends AbstractHttpConfigurer<OAuth2LoginConfigurer, HttpSecurity> {

    private final ThirdPartyAuthenticationProvider thirdPartyAuthenticationProvider;

    @Override
    public void configure(HttpSecurity http) {
        // 配置OAuth2登录
        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
                .authorizationEndpoint(authorization -> authorization
                        .baseUri("/oauth2/authorization"))
                .redirectionEndpoint(redirection -> redirection
                        .baseUri("/login/oauth2/code/*"))
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(thirdPartyAuthenticationProvider)));

        // 添加OAuth2登录过滤器
        http.addFilterBefore(
                new OAuth2LoginAuthenticationFilter(
                        http.getSharedObject(org.springframework.security.oauth2.client.OAuth2AuthorizedClientService.class),
                        http.getSharedObject(org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository.class)),
                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void init(HttpSecurity http) {
        // 初始化配置
        http.authenticationProvider(thirdPartyAuthenticationProvider);
    }
} 