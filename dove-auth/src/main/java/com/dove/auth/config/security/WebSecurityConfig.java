package com.dove.auth.config.security;

import com.dove.auth.core.security.AbnormalLoginDetector;
import com.dove.auth.core.security.SecurityEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Web安全配置
 */
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final SecurityEventPublisher securityEventPublisher;
    private final AbnormalLoginDetector abnormalLoginDetector;

    /**
     * 配置Web安全
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(
                        "/favicon.ico",
                        "/error",
                        "/static/**",
                        "/webjars/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                );
    }

    /**
     * 配置认证成功处理器
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // 发布认证成功事件
            securityEventPublisher.publishAuthenticationSuccess(authentication);
            // 检测异常登录
            abnormalLoginDetector.detectAbnormalLogin(authentication);
            // 重定向到首页
            response.sendRedirect("/");
        };
    }

    /**
     * 配置认证失败处理器
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            // 发布认证失败事件
            securityEventPublisher.publishAuthenticationFailure(exception, request);
            // 重定向到登录页
            response.sendRedirect("/login?error");
        };
    }

    /**
     * 配置登出成功处理器
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            // 发布登出成功事件
            securityEventPublisher.publishLogoutSuccess(authentication);
            // 重定向到登录页
            response.sendRedirect("/login?logout");
        };
    }

    /**
     * 配置认证事件发布器
     */
    @Bean
    public DefaultAuthenticationEventPublisher authenticationEventPublisher() {
        return new DefaultAuthenticationEventPublisher();
    }
} 