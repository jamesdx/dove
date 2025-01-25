package com.helix.dove.auth.service;

import com.helix.dove.auth.entity.User;

public interface UserService {
    
    /**
     * 根据邮箱查找用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User findByEmail(String email);
    
    /**
     * 更新用户最后登录时间
     *
     * @param userId 用户ID
     */
    void updateLastLoginTime(Long userId);
    
    /**
     * 更新用户设置
     *
     * @param user 用户信息
     */
    void updateUserSettings(User user);
} 