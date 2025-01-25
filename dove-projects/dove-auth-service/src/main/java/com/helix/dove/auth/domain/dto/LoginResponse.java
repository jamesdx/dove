package com.helix.dove.auth.domain.dto;

import com.helix.dove.auth.domain.entity.User;
import lombok.Data;

@Data
public class LoginResponse {

    private String token;

    private User user;

    public static LoginResponse of(String token, User user) {
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUser(user);
        return response;
    }
} 