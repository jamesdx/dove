package com.helix.dove.auth.service;

public interface PasswordService {
    
    /**
     * 发送密码重置邮件
     *
     * @param email 用户邮箱
     */
    void sendResetPasswordEmail(String email);
    
    /**
     * 验证重置密码令牌
     *
     * @param token 重置令牌
     * @return 是否有效
     */
    boolean validateResetToken(String token);
    
    /**
     * 重置密码
     *
     * @param token 重置令牌
     * @param newPassword 新密码
     */
    void resetPassword(String token, String newPassword);
    
    /**
     * 修改密码
     *
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);
} 