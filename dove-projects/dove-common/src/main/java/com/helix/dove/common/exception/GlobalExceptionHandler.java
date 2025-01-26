package com.helix.dove.common.exception;

import com.helix.dove.common.api.CommonResult;
import com.helix.dove.common.api.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global Exception Handler
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = GlobalException.class)
    public CommonResult<String> handle(GlobalException e) {
        log.error("Global exception: {}", e.getMessage(), e);
        if (e.getErrorCode() != null) {
            return CommonResult.failed(e.getErrorCode());
        }
        return CommonResult.failed(e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CommonResult<String> handleValidException(MethodArgumentNotValidException e) {
        log.error("Parameter validation exception: {}", e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField()+fieldError.getDefaultMessage();
            }
        }
        return CommonResult.validateFailed(message);
    }

    @ExceptionHandler(value = BindException.class)
    public CommonResult<String> handleValidException(BindException e) {
        log.error("Parameter validation exception: {}", e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField()+fieldError.getDefaultMessage();
            }
        }
        return CommonResult.validateFailed(message);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public CommonResult<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Illegal argument exception: {}", e.getMessage(), e);
        return CommonResult.validateFailed(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public CommonResult<String> handleException(Exception e) {
        log.error("System exception: {}", e.getMessage(), e);
        return CommonResult.failed(ResultCode.SYSTEM_ERROR);
    }
} 