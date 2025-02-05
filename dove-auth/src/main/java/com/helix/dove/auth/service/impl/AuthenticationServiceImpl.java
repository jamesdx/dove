package com.helix.dove.auth.service.impl;

import com.helix.dove.auth.dto.LoginRequest;
import com.helix.dove.auth.dto.TokenResponse;
import com.helix.dove.auth.service.AuthenticationService;
import com.helix.dove.auth.service.TokenService;
import com.helix.dove.auth.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;

    public AuthenticationServiceImpl(
            AuthenticationManager authenticationManager,
            TokenService tokenService,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            String accessToken = tokenService.generateAccessToken(authentication);
            String refreshToken = tokenService.generateRefreshToken(authentication);
            
            userService.recordLogin(((User) authentication.getPrincipal()).getId());
            
            return new TokenResponse(accessToken, refreshToken);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        if (!tokenService.validateToken(refreshToken) || !tokenService.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String username = tokenService.getUsernameFromToken(refreshToken);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userService.loadUserByUsername(username),
            null,
            userService.loadUserByUsername(username).getAuthorities()
        );

        String newAccessToken = tokenService.generateAccessToken(authentication);
        String newRefreshToken = tokenService.generateRefreshToken(authentication);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    @Override
    public void logout(String accessToken) {
        if (tokenService.validateToken(accessToken)) {
            SecurityContextHolder.clearContext();
        }
    }

    @Override
    public boolean validateToken(String token) {
        return tokenService.validateToken(token);
    }
} 