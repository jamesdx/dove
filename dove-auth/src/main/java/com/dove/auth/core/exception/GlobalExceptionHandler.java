package com.dove.auth.core.exception;

import com.dove.common.core.exception.BaseException;
import com.dove.common.core.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    public Result<Void> handleAuthenticationException(AuthenticationException e) {
        log.error("认证异常", e);
        String message = e instanceof BadCredentialsException ? "用户名或密码错误" : e.getMessage();
        return Result.error(message);
    }

    /**
     * 处理授权异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e) {
        log.error("授权异常", e);
        return Result.error("没有权限访问该资源");
    }

    /**
     * 处理Token异常
     */
    @ExceptionHandler(TokenException.class)
    public Result<Void> handleTokenException(TokenException e) {
        log.error("Token异常", e);
        return Result.error(e.getMessage());
    }

    /**
     * 处理验证码异常
     */
    @ExceptionHandler(CaptchaException.class)
    public Result<Void> handleCaptchaException(CaptchaException e) {
        log.error("验证码异常", e);
        return Result.error(e.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    public Result<Void> handleValidationException(Exception e) {
        log.error("参数校验异常", e);
        return Result.error("参数校验失败");
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BaseException.class)
    public Result<Void> handleBaseException(BaseException e) {
        log.error("业务异常", e);
        return Result.error(e.getMessage());
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error("系统繁忙，请稍后重试");
    }
} 