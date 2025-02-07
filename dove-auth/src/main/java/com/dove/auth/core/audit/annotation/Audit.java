package com.dove.auth.core.audit.annotation;

import java.lang.annotation.*;

/**
 * 审计注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Audit {

    /**
     * 审计类型
     */
    String type() default "";

    /**
     * 审计描述
     */
    String description() default "";
} 