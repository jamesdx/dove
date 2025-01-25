package com.helix.dove.auth.service.impl;

import com.helix.dove.auth.dto.LoginResponse;
import com.helix.dove.auth.dto.SocialLoginRequest;
import com.helix.dove.auth.entity.User;
import com.helix.dove.auth.service.SocialLoginService;
import com.helix.dove.auth.service.UserService;
import com.helix.dove.auth.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialLoginServiceImpl implements SocialLoginService {

    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RestTemplate restTemplate;

    @Override
    public LoginResponse loginWithGoogle(SocialLoginRequest request) {
        // 验证Google Token并获取用户信息
        // TODO: 实现Google Token验证和用户信息获取
        return processLogin("google_user@example.com", request);
    }

    @Override
    public LoginResponse loginWithMicrosoft(SocialLoginRequest request) {
        // 验证Microsoft Token并获取用户信息
        // TODO: 实现Microsoft Token验证和用户信息获取
        return processLogin("microsoft_user@example.com", request);
    }

    @Override
    public LoginResponse loginWithApple(SocialLoginRequest request) {
        // 验证Apple Token并获取用户信息
        // TODO: 实现Apple Token验证和用户信息获取
        return processLogin("apple_user@example.com", request);
    }

    @Override
    public LoginResponse loginWithSlack(SocialLoginRequest request) {
        // 验证Slack Token并获取用户信息
        // TODO: 实现Slack Token验证和用户信息获取
        return processLogin("slack_user@example.com", request);
    }

    private LoginResponse processLogin(String email, SocialLoginRequest request) {
        // 查找或创建用户
        User user = userService.findByEmail(email);
        if (user == null) {
            // TODO: 创建新用户
        }

        // 创建认证信息
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            email,
            null,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成Token
        String accessToken = jwtUtils.generateAccessToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(authentication);

        // 保存刷新令牌到Redis
        String key = "refresh_token:" + authentication.getName();
        redisTemplate.opsForValue().set(key, refreshToken, 7, TimeUnit.DAYS);

        // 更新最后登录时间
        userService.updateLastLoginTime(user.getId());

        // 构建响应
        return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .expiresIn(7200L)
            .userSettings(LoginResponse.UserSettings.builder()
                .locale(request.getLocale())
                .timezone(request.getTimezone())
                .dateFormat("yyyy-MM-dd")
                .timeFormat("HH:mm:ss")
                .currency("USD")
                .build())
            .build();
    }
} 