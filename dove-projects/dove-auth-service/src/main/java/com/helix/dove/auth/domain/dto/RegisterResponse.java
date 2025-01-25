package com.helix.dove.auth.domain.dto;

import com.helix.dove.auth.domain.entity.User;
import lombok.Data;

@Data
public class RegisterResponse {
    private String message;
    private User user;

    public static RegisterResponse of(String message, User user) {
        RegisterResponse response = new RegisterResponse();
        response.setMessage(message);
        response.setUser(user);
        return response;
    }
} 