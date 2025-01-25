package com.helix.dove.auth.config;

import com.helix.dove.auth.tenant.TenantContextHolder;
import com.helix.dove.auth.tenant.TenantInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class TenantConfig implements WebMvcConfigurer {

    @Bean
    public TenantContextHolder tenantContextHolder() {
        return new TenantContextHolder();
    }

    @Bean
    public TenantInterceptor tenantInterceptor() {
        return new TenantInterceptor(tenantContextHolder());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns("/api/v1/auth/login", "/api/v1/auth/refresh", "/error");
    }
} 