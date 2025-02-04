package com.helix.dove.auth.controller;

import com.helix.dove.auth.dto.LoginRequest;
import com.helix.dove.auth.dto.TokenResponse;
import com.helix.dove.auth.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user and return JWT tokens")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Invalidate the user's token")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        String bearerToken = token.substring(7);
        authenticationService.logout(bearerToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Get new access token using refresh token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestHeader("Authorization") String token) {
        String refreshToken = token.substring(7);
        return ResponseEntity.ok(authenticationService.refreshToken(refreshToken));
    }

    @GetMapping("/validate")
    @Operation(summary = "Validate token", description = "Validate if the token is still valid")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        String bearerToken = token.substring(7);
        return ResponseEntity.ok(authenticationService.validateToken(bearerToken));
    }
} 