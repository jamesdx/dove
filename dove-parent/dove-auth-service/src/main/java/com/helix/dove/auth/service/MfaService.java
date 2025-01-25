package com.helix.dove.auth.service;

public interface MfaService {
    
    /**
     * 生成MFA密钥
     *
     * @param userId 用户ID
     * @return MFA密钥和二维码URL
     */
    MfaSetupResult generateMfaSecret(Long userId);
    
    /**
     * 启用MFA
     *
     * @param userId 用户ID
     * @param code 验证码
     */
    void enableMfa(Long userId, String code);
    
    /**
     * 禁用MFA
     *
     * @param userId 用户ID
     * @param code 验证码
     */
    void disableMfa(Long userId, String code);
    
    /**
     * 验证MFA代码
     *
     * @param userId 用户ID
     * @param code 验证码
     * @return 是否有效
     */
    boolean verifyCode(Long userId, String code);
}

record MfaSetupResult(String secret, String qrCodeUrl) {} 