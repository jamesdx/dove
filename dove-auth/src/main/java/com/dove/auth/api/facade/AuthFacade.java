package com.dove.auth.api.facade;

import com.dove.auth.api.dto.request.LoginRequest;
import com.dove.auth.api.dto.response.LoginResponse;
import com.dove.common.web.response.Result;

/**
 * 认证服务外观接口
 */
public interface AuthFacade {

    /**
     * 登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    Result<LoginResponse> login(LoginRequest request);

    /**
     * 发送验证码
     *
     * @param type 验证码类型(SMS/EMAIL)
     * @param target 目标(手机号/邮箱)
     * @return 结果
     */
    Result<Void> sendCode(String type, String target);

    /**
     * 验证验证码
     *
     * @param type 验证码类型(SMS/EMAIL)
     * @param target 目标(手机号/邮箱)
     * @param code 验证码
     * @return 结果
     */
    Result<Void> verifyCode(String type, String target, String code);

    /**
     * 刷新令牌
     *
     * @param refreshToken 刷新令牌
     * @return 登录响应
     */
    Result<LoginResponse> refreshToken(String refreshToken);

    /**
     * 登出
     *
     * @return 结果
     */
    Result<Void> logout();

    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    Result<Object> getCurrentUser();

    /**
     * 检查令牌有效性
     *
     * @param token 访问令牌
     * @return 结果
     */
    Result<Void> checkToken(String token);
} 