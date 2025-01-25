package com.helix.dove.auth.service.impl;

import com.helix.dove.auth.dto.LoginRequest;
import com.helix.dove.auth.dto.LoginResponse;
import com.helix.dove.auth.service.AuthService;
import com.helix.dove.auth.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public LoginResponse login(LoginRequest request) {
        // 认证
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成Token
        String accessToken = jwtUtils.generateAccessToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(authentication);

        // 保存刷新令牌到Redis
        String key = "refresh_token:" + authentication.getName();
        redisTemplate.opsForValue().set(key, refreshToken, 7, TimeUnit.DAYS);

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

    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String key = "refresh_token:" + authentication.getName();
            redisTemplate.delete(key);
        }
        SecurityContextHolder.clearContext();
    }

    @Override
    public LoginResponse refresh(String token) {
        // 验证刷新令牌
        if (!token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid token format");
        }
        String refreshToken = token.substring(7);
        String username = jwtUtils.getUsernameFromToken(refreshToken);
        
        // 检查Redis中的刷新令牌
        String key = "refresh_token:" + username;
        String storedToken = (String) redisTemplate.opsForValue().get(key);
        if (!refreshToken.equals(storedToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        // 生成新的访问令牌
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String newAccessToken = jwtUtils.generateAccessToken(authentication);

        return LoginResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(refreshToken)
            .expiresIn(7200L)
            .build();
    }
} 