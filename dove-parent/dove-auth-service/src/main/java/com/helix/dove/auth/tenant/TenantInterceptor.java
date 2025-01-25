package com.helix.dove.auth.tenant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class TenantInterceptor implements HandlerInterceptor {

    private final TenantContextHolder tenantContextHolder;
    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader(TENANT_HEADER);
        if (StringUtils.hasText(tenantId)) {
            tenantContextHolder.setTenantId(tenantId);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        tenantContextHolder.clear();
    }
} 