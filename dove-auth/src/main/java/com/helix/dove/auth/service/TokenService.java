package com.helix.dove.auth.service;

import org.springframework.security.core.Authentication;

public interface TokenService {
    
    String generateAccessToken(Authentication authentication);
    
    String generateRefreshToken(Authentication authentication);
    
    boolean validateToken(String token);
    
    String getUsernameFromToken(String token);
    
    boolean isRefreshToken(String token);
    
    long getExpirationTime(String token);
} 