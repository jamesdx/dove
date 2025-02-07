package com.dove.auth.core.exception;

import com.dove.common.core.exception.BaseException;

/**
 * 认证异常基类
 */
public class AuthException extends BaseException {

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
} 