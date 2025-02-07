package com.dove.auth.core.auth.token;

import com.dove.common.core.model.LoginUser;
import com.dove.common.core.utils.JsonUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Token服务
 */
@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenProperties tokenProperties;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 创建令牌
     */
    public String createToken(LoginUser loginUser) {
        String token = UUID.randomUUID().toString();
        loginUser.setToken(token);
        refreshToken(loginUser);

        // 判断是否开启多端登录
        if (!tokenProperties.isMultiLogin()) {
            // 如果不允许多端登录，则清除该用户的其他token
            String userIdKey = tokenProperties.getRedisKeyPrefix() + "user_id:" + loginUser.getUserId();
            String oldToken = (String) redisTemplate.opsForValue().get(userIdKey);
            if (StringUtils.isNotEmpty(oldToken)) {
                redisTemplate.delete(tokenProperties.getRedisKeyPrefix() + oldToken);
            }
            redisTemplate.opsForValue().set(userIdKey, token);
        }

        // 判断是否使用JWT存储策略
        if ("jwt".equals(tokenProperties.getStoreStrategy())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("login_user", JsonUtils.toJson(loginUser));
            return createJwtToken(claims);
        }

        // 默认使用Redis存储策略
        String tokenKey = tokenProperties.getRedisKeyPrefix() + token;
        redisTemplate.opsForValue().set(tokenKey, loginUser, tokenProperties.getExpireTime(), TimeUnit.MINUTES);
        return token;
    }

    /**
     * 刷新令牌有效期
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + tokenProperties.getExpireTime() * 60 * 1000L);

        // 判断是否使用JWT存储策略
        if ("jwt".equals(tokenProperties.getStoreStrategy())) {
            return;
        }

        // 刷新Redis中的用户信息
        String tokenKey = tokenProperties.getRedisKeyPrefix() + loginUser.getToken();
        redisTemplate.opsForValue().set(tokenKey, loginUser, tokenProperties.getExpireTime(), TimeUnit.MINUTES);
    }

    /**
     * 验证令牌有效期
     */
    public boolean verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= tokenProperties.getRefreshThreshold() * 60 * 1000L) {
            refreshToken(loginUser);
            return true;
        }
        return expireTime - currentTime > 0;
    }

    /**
     * 从令牌中获取数据声明
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(tokenProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从数据声明生成令牌
     */
    private String createJwtToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, tokenProperties.getSecret())
                .setExpiration(new java.util.Date(System.currentTimeMillis() + tokenProperties.getExpireTime() * 60 * 1000L))
                .compact();
    }

    /**
     * 获取用户身份信息
     */
    public LoginUser getLoginUser(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        // 判断是否使用JWT存储策略
        if ("jwt".equals(tokenProperties.getStoreStrategy())) {
            Claims claims = parseToken(token);
            String userStr = claims.get("login_user", String.class);
            return JsonUtils.fromJson(userStr, LoginUser.class);
        }

        // 从Redis中获取用户信息
        String tokenKey = tokenProperties.getRedisKeyPrefix() + token;
        return (LoginUser) redisTemplate.opsForValue().get(tokenKey);
    }

    /**
     * 删除用户身份信息
     */
    public void removeToken(LoginUser loginUser) {
        if (loginUser != null) {
            String token = loginUser.getToken();
            if (StringUtils.isNotEmpty(token)) {
                String tokenKey = tokenProperties.getRedisKeyPrefix() + token;
                redisTemplate.delete(tokenKey);

                // 如果不允许多端登录，则清除用户ID对应的token
                if (!tokenProperties.isMultiLogin()) {
                    String userIdKey = tokenProperties.getRedisKeyPrefix() + "user_id:" + loginUser.getUserId();
                    redisTemplate.delete(userIdKey);
                }
            }
        }
    }
} 