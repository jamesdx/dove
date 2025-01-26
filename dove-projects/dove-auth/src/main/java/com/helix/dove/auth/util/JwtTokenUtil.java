package com.helix.dove.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Token Utility
 */
@Slf4j
@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.token-prefix:Bearer}")
    private String tokenPrefix;

    private Key key;

    public JwtTokenUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generate token
     */
    public String generateToken(String username, String tenantCode, boolean rememberMe) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("tenantCode", tenantCode);
        return generateToken(claims, rememberMe);
    }

    /**
     * Generate token from claims
     */
    private String generateToken(Map<String, Object> claims, boolean rememberMe) {
        long expirationTime = rememberMe ? expiration * 7 : expiration;
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Get claims from token
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("JWT token parse error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Get username from token
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.get("username", String.class) : null;
    }

    /**
     * Get tenant code from token
     */
    public String getTenantCodeFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.get("tenantCode", String.class) : null;
    }

    /**
     * Validate token
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims != null && !isTokenExpired(claims);
        } catch (Exception e) {
            log.error("JWT token validation error: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if token is expired
     */
    private boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * Format token with prefix
     */
    public String formatToken(String token) {
        return tokenPrefix + " " + token;
    }

    /**
     * Remove token prefix
     */
    public String removeTokenPrefix(String token) {
        if (token != null && token.startsWith(tokenPrefix + " ")) {
            return token.substring(tokenPrefix.length() + 1);
        }
        return token;
    }
} 