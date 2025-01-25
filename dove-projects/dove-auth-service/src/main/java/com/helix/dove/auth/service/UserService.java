package com.helix.dove.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.helix.dove.auth.domain.dto.LoginRequest;
import com.helix.dove.auth.domain.dto.LoginResponse;
import com.helix.dove.auth.domain.entity.User;

public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest request);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User getByEmail(String email);

    /**
     * 更新用户最后登录时间
     *
     * @param userId 用户ID
     */
    void updateLastLoginTime(Long userId);
} 