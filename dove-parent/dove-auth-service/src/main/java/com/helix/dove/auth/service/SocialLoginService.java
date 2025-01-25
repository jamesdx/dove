package com.helix.dove.auth.service;

import com.helix.dove.auth.dto.LoginResponse;
import com.helix.dove.auth.dto.SocialLoginRequest;

public interface SocialLoginService {
    
    /**
     * Google登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    LoginResponse loginWithGoogle(SocialLoginRequest request);
    
    /**
     * Microsoft登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    LoginResponse loginWithMicrosoft(SocialLoginRequest request);
    
    /**
     * Apple登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    LoginResponse loginWithApple(SocialLoginRequest request);
    
    /**
     * Slack登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    LoginResponse loginWithSlack(SocialLoginRequest request);
} 