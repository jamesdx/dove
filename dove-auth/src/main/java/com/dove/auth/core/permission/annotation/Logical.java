package com.dove.auth.core.permission.annotation;

/**
 * 权限验证逻辑
 */
public enum Logical {
    /**
     * 必须具有所有权限
     */
    AND,

    /**
     * 具有任一权限即可
     */
    OR
} 