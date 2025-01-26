package com.helix.dove.common.api;

import lombok.Getter;

/**
 * API Response Code Enumeration
 */
@Getter
public enum ResultCode {
    SUCCESS(200, "Success"),
    FAILED(500, "Failed"),
    VALIDATE_FAILED(404, "Parameter validation failed"),
    UNAUTHORIZED(401, "Not logged in or token has expired"),
    FORBIDDEN(403, "No relevant permissions"),
    
    // Login related
    USERNAME_OR_PASSWORD_INCORRECT(1001, "Username or password is incorrect"),
    ACCOUNT_LOCKED(1002, "Account has been locked"),
    ACCOUNT_NOT_EXIST(1003, "Account does not exist"),
    ACCOUNT_EXPIRED(1004, "Account has expired"),
    CREDENTIALS_EXPIRED(1005, "Credentials have expired"),
    ACCOUNT_DISABLED(1006, "Account is disabled"),
    
    // Token related
    TOKEN_INVALID(2001, "Invalid token"),
    TOKEN_EXPIRED(2002, "Token has expired"),
    TOKEN_USED(2003, "Token has been used"),
    TOKEN_NOT_EXIST(2004, "Token does not exist"),
    
    // Verification related
    VERIFY_CODE_INCORRECT(3001, "Verification code is incorrect"),
    VERIFY_CODE_EXPIRED(3002, "Verification code has expired"),
    VERIFY_CODE_USED(3003, "Verification code has been used"),
    
    // Rate limiting
    TOO_MANY_REQUESTS(4001, "Too many requests"),
    
    // System error
    SYSTEM_ERROR(5001, "System error"),
    SERVICE_UNAVAILABLE(5002, "Service is temporarily unavailable"),
    GATEWAY_ERROR(5003, "Gateway error");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
} 