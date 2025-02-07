package com.dove.auth.core.auth.context;

/**
 * 认证上下文持有者
 * 使用ThreadLocal存储认证上下文信息
 */
public class AuthenticationContextHolder {

    private static final ThreadLocal<AuthenticationContext> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 获取认证上下文
     */
    public static AuthenticationContext getContext() {
        AuthenticationContext context = CONTEXT_HOLDER.get();
        if (context == null) {
            context = new AuthenticationContext();
            CONTEXT_HOLDER.set(context);
        }
        return context;
    }

    /**
     * 设置认证上下文
     */
    public static void setContext(AuthenticationContext context) {
        CONTEXT_HOLDER.set(context);
    }

    /**
     * 清除认证上下文
     */
    public static void clearContext() {
        CONTEXT_HOLDER.remove();
    }

    private AuthenticationContextHolder() {
        throw new IllegalStateException("Utility class");
    }
} 