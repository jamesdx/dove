package com.helix.dove.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Multi-tenant Configuration
 */
@Configuration
public class TenantConfig implements WebFilter {

    private static final String TENANT_HEADER = "X-Tenant-ID";
    private static final String DEFAULT_TENANT = "default";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String tenantId = request.getHeaders().getFirst(TENANT_HEADER);

        // If tenant ID is not provided, use default tenant
        if (tenantId == null) {
            tenantId = DEFAULT_TENANT;
        }

        // Add tenant ID to the request attributes
        exchange.getAttributes().put(TENANT_HEADER, tenantId);

        // Add tenant ID to the request headers
        ServerHttpRequest mutatedRequest = request.mutate()
                .header(TENANT_HEADER, tenantId)
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }
} 