package com.helix.dove.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helix.dove.auth.config.SecurityConfig;
import com.helix.dove.auth.dto.LoginRequest;
import com.helix.dove.auth.dto.TokenResponse;
import com.helix.dove.auth.service.AuthenticationService;
import com.helix.dove.auth.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
    "spring.cloud.nacos.discovery.enabled=false",
    "spring.cloud.nacos.config.enabled=false",
    "spring.config.import=optional:nacos:",
    "jwt.secret=testSecretKeyForJwtTokenGenerationAndValidation",
    "jwt.expiration=3600000"
})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void loginSuccess() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        Authentication authentication = new UsernamePasswordAuthenticationToken("testuser", null);
        when(jwtUtil.generateToken(any(Authentication.class))).thenReturn("test-access-token");
        when(jwtUtil.generateRefreshToken(any(Authentication.class))).thenReturn("test-refresh-token");
        when(jwtUtil.getExpirationTime()).thenReturn(3600L);

        TokenResponse tokenResponse = TokenResponse.builder()
            .accessToken("test-access-token")
            .refreshToken("test-refresh-token")
            .tokenType("Bearer")
            .expiresIn(3600)
            .build();

        when(authenticationService.login(any(LoginRequest.class))).thenReturn(tokenResponse);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("test-access-token"))
            .andExpect(jsonPath("$.refreshToken").value("test-refresh-token"))
            .andExpect(jsonPath("$.tokenType").value("Bearer"))
            .andExpect(jsonPath("$.expiresIn").value(3600));
    }

    @Test
    void loginFailureWithInvalidRequest() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        // Missing required fields

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
            .andExpect(status().isBadRequest());
    }
} 