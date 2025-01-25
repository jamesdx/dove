package com.helix.dove.auth.service;

import com.helix.dove.auth.dto.LoginRequest;
import com.helix.dove.auth.dto.LoginResponse;

public interface AuthService {
    
    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest request);

    /**
     * 用户登出
     */
    void logout();

    /**
     * 刷新Token
     *
     * @param token 刷新令牌
     * @return 登录响应
     */
    LoginResponse refresh(String token);
} 