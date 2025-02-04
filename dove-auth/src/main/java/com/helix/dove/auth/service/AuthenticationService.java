package com.helix.dove.auth.service;

import com.helix.dove.auth.dto.LoginRequest;
import com.helix.dove.auth.dto.TokenResponse;

public interface AuthenticationService {
    TokenResponse login(LoginRequest loginRequest);
    void logout(String token);
    TokenResponse refreshToken(String refreshToken);
    boolean validateToken(String token);
} 