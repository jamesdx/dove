package com.dove.auth.core.config;

import com.dove.auth.core.auth.LoginTypeDetector;
import com.dove.auth.core.auth.captcha.CaptchaService;
import com.dove.auth.core.auth.filter.TokenAuthenticationFilter;
import com.dove.auth.core.auth.handler.*;
import com.dove.auth.core.auth.provider.DefaultAuthenticationProvider;
import com.dove.auth.core.auth.token.TokenGenerator;
import com.dove.auth.core.auth.token.TokenService;
import com.dove.auth.core.security.SecurityEventPublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 认证自动配置
 */
@EnableAsync
@Configuration
public class AuthAutoConfiguration {

    /**
     * 密码编码器
     */
    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Token生成器
     */
    @Bean
    @ConditionalOnMissingBean
    public TokenGenerator tokenGenerator() {
        return new TokenGenerator();
    }

    /**
     * Token服务
     */
    @Bean
    @ConditionalOnMissingBean
    public TokenService tokenService(TokenGenerator tokenGenerator, RedisTemplate<String, Object> redisTemplate) {
        return new TokenService(tokenGenerator, redisTemplate);
    }

    /**
     * 登录类型检测器
     */
    @Bean
    @ConditionalOnMissingBean
    public LoginTypeDetector loginTypeDetector() {
        return new LoginTypeDetector();
    }

    /**
     * 验证码服务
     */
    @Bean
    @ConditionalOnMissingBean
    public CaptchaService captchaService(RedisTemplate<String, Object> redisTemplate) {
        return new CaptchaService(redisTemplate);
    }

    /**
     * 安全事件发布器
     */
    @Bean
    @ConditionalOnMissingBean
    public SecurityEventPublisher securityEventPublisher() {
        return new SecurityEventPublisher();
    }

    /**
     * 认证提供者
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder,
                                                             LoginTypeDetector loginTypeDetector,
                                                             SecurityEventPublisher eventPublisher) {
        return new DefaultAuthenticationProvider(passwordEncoder, loginTypeDetector, eventPublisher);
    }

    /**
     * Token认证过滤器
     */
    @Bean
    @ConditionalOnMissingBean
    public TokenAuthenticationFilter tokenAuthenticationFilter(TokenService tokenService,
                                                            SecurityEventPublisher eventPublisher) {
        return new TokenAuthenticationFilter(tokenService, eventPublisher);
    }

    /**
     * 认证成功处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationSuccessHandler authenticationSuccessHandler(TokenService tokenService,
                                                                   SecurityEventPublisher eventPublisher) {
        return new AuthenticationSuccessHandler(tokenService, eventPublisher);
    }

    /**
     * 认证失败处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationFailureHandler authenticationFailureHandler(SecurityEventPublisher eventPublisher) {
        return new AuthenticationFailureHandler(eventPublisher);
    }

    /**
     * 登出成功处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public LogoutSuccessHandler logoutSuccessHandler(TokenService tokenService,
                                                    SecurityEventPublisher eventPublisher) {
        return new LogoutSuccessHandler(tokenService, eventPublisher);
    }

    /**
     * 访问拒绝处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public AccessDeniedHandler accessDeniedHandler(SecurityEventPublisher eventPublisher) {
        return new AccessDeniedHandler(eventPublisher);
    }

    /**
     * 认证入口点
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint();
    }
} 