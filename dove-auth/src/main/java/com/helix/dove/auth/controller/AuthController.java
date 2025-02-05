package com.helix.dove.auth.controller;

import com.helix.dove.auth.dto.LoginRequest;
import com.helix.dove.auth.dto.TokenResponse;
import com.helix.dove.auth.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication API endpoints")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return access token")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = authenticationService.login(loginRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Get new access token using refresh token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestHeader("Authorization") String authorization) {
        String refreshToken = authorization.substring(7); // Remove "Bearer " prefix
        TokenResponse tokenResponse = authenticationService.refreshToken(refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Invalidate the current session")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.substring(7); // Remove "Bearer " prefix
        authenticationService.logout(accessToken);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate")
    @Operation(summary = "Validate token", description = "Check if the provided token is valid")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7); // Remove "Bearer " prefix
        boolean isValid = authenticationService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }
} 