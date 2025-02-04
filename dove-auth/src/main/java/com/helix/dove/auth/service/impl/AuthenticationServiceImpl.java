package com.helix.dove.auth.service.impl;

import com.helix.dove.auth.dto.LoginRequest;
import com.helix.dove.auth.dto.TokenResponse;
import com.helix.dove.auth.service.AuthenticationService;
import com.helix.dove.auth.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        String accessToken = jwtUtil.generateToken(authentication);
        String refreshToken = jwtUtil.generateRefreshToken(authentication);
        
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getExpirationTime())
                .build();
    }

    @Override
    public void logout(String token) {
        // Implement token invalidation logic here
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        Claims claims = jwtUtil.validateToken(refreshToken);
        if (claims == null) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        
        String username = claims.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities()
        );
        
        String newAccessToken = jwtUtil.generateToken(authentication);
        String newRefreshToken = jwtUtil.generateRefreshToken(authentication);
        
        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getExpirationTime())
                .build();
    }

    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token) != null;
    }
} 