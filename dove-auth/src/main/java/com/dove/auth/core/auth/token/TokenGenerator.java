package com.dove.auth.core.auth.token;

import com.dove.common.core.model.LoginUser;
import com.dove.common.core.utils.JsonUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Token生成器
 */
@Component
@RequiredArgsConstructor
public class TokenGenerator {

    private final TokenProperties tokenProperties;

    /**
     * 生成Token
     */
    public String generateToken(LoginUser loginUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", JsonUtils.toJson(loginUser));
        return createToken(claims);
    }

    /**
     * 生成刷新Token
     */
    public String generateRefreshToken(LoginUser loginUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", loginUser.getUserId());
        claims.put("type", "refresh");
        return createToken(claims, tokenProperties.getRefreshExpireTime() * 24 * 60L);
    }

    /**
     * 从Token中获取登录用户信息
     */
    public LoginUser getLoginUser(String token) {
        Claims claims = parseToken(token);
        String userJson = claims.get("user", String.class);
        return JsonUtils.fromJson(userJson, LoginUser.class);
    }

    /**
     * 验证Token是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return !isTokenExpired(claims);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断Token是否需要刷新
     */
    public boolean needRefresh(String token) {
        Claims claims = parseToken(token);
        Date expiration = claims.getExpiration();
        long now = System.currentTimeMillis();
        return (expiration.getTime() - now) / (1000 * 60) <= tokenProperties.getRefreshThreshold();
    }

    /**
     * 创建Token
     */
    private String createToken(Map<String, Object> claims) {
        return createToken(claims, tokenProperties.getExpireTime());
    }

    /**
     * 创建Token
     */
    private String createToken(Map<String, Object> claims, long expireMinutes) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expireMinutes * 60 * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, tokenProperties.getSecret())
                .compact();
    }

    /**
     * 解析Token
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(tokenProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 判断Token是否过期
     */
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
} 