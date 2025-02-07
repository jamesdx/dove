package com.dove.auth.core.permission.annotation;

import java.lang.annotation.*;

/**
 * 权限注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequiresPermissions {

    /**
     * 权限字符串
     */
    String[] value();

    /**
     * 验证模式：AND | OR
     */
    Logical logical() default Logical.AND;

    /**
     * 验证失败时的错误消息
     */
    String message() default "没有操作权限";
} 