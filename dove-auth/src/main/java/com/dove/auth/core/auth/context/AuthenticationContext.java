package com.dove.auth.core.auth.context;

import com.dove.common.core.model.LoginUser;
import lombok.Getter;
import lombok.Setter;

/**
 * 认证上下文
 * 用于在认证过程中传递认证相关的信息
 */
@Getter
@Setter
public class AuthenticationContext {

    /**
     * 登录用户
     */
    private LoginUser loginUser;

    /**
     * 登录类型
     */
    private String loginType;

    /**
     * 登录IP
     */
    private String loginIp;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 设备信息
     */
    private String deviceInfo;

    /**
     * 验证码ID
     */
    private String captchaId;

    /**
     * 验证码
     */
    private String captchaCode;

    /**
     * 是否记住我
     */
    private Boolean rememberMe;

    /**
     * 附加信息
     */
    private Object extra;
} 