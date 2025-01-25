package com.helix.dove.auth.tenant;

import org.springframework.core.NamedThreadLocal;

public class TenantContextHolder {
    private static final ThreadLocal<String> CONTEXT = new NamedThreadLocal<>("Tenant Context");

    public void setTenantId(String tenantId) {
        CONTEXT.set(tenantId);
    }

    public String getTenantId() {
        return CONTEXT.get();
    }

    public void clear() {
        CONTEXT.remove();
    }
} 