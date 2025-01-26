package com.helix.dove.common.exception;

import com.helix.dove.common.api.ResultCode;
import lombok.Getter;

/**
 * Global Custom Exception
 */
@Getter
public class GlobalException extends RuntimeException {
    private final ResultCode errorCode;

    public GlobalException(ResultCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public GlobalException(String message) {
        super(message);
        this.errorCode = ResultCode.FAILED;
    }

    public GlobalException(Throwable cause) {
        super(cause);
        this.errorCode = ResultCode.FAILED;
    }

    public GlobalException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = ResultCode.FAILED;
    }

    public GlobalException(ResultCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public GlobalException(ResultCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public GlobalException(ResultCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
} 