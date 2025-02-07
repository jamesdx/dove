package com.dove.auth.core.exception;

/**
 * Token异常
 */
public class TokenException extends AuthException {

    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class TokenExpiredException extends TokenException {
        public TokenExpiredException() {
            super("Token已过期");
        }
    }

    public static class TokenInvalidException extends TokenException {
        public TokenInvalidException() {
            super("Token无效");
        }
    }

    public static class TokenNotFoundException extends TokenException {
        public TokenNotFoundException() {
            super("Token不存在");
        }
    }
} 