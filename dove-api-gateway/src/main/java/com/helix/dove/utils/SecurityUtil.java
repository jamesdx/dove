package com.helix.dove.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SecurityUtil {
    
    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String X_REAL_IP = "X-Real-IP";
    private static final Set<String> INTERNAL_ENDPOINTS = Set.of(
            "/actuator", "/internal", "/admin"
    );

    public static String getClientIp(ServerHttpRequest request) {
        List<String> forwardedFor = request.getHeaders().get(X_FORWARDED_FOR);
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            return forwardedFor.get(0);
        }

        List<String> realIp = request.getHeaders().get(X_REAL_IP);
        if (realIp != null && !realIp.isEmpty()) {
            return realIp.get(0);
        }

        return request.getRemoteAddress() != null ? 
               request.getRemoteAddress().getAddress().getHostAddress() : 
               "unknown";
    }

    public static Mono<Authentication> getCurrentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication);
    }

    public static boolean isInternalEndpoint(String path) {
        return INTERNAL_ENDPOINTS.stream()
                .anyMatch(path::startsWith);
    }

    public static HttpHeaders sanitizeHeaders(HttpHeaders headers) {
        HttpHeaders sanitized = new HttpHeaders();
        headers.forEach((key, value) -> {
            if (!key.toLowerCase().startsWith("x-internal-")) {
                sanitized.put(key, value);
            }
        });
        return sanitized;
    }

    public static boolean hasRequiredRole(Authentication authentication, String requiredRole) {
        return authentication != null &&
               authentication.getAuthorities().stream()
                           .anyMatch(a -> a.getAuthority().equals("ROLE_" + requiredRole));
    }

    public static Map<String, Object> extractTokenClaims(String token) {
        // TODO: Implement JWT token claims extraction
        return Map.of();
    }

    public static boolean isSecureConnection(ServerWebExchange exchange) {
        return exchange.getRequest().getSslInfo() != null;
    }

    public static String maskSensitiveData(String data) {
        if (data == null || data.length() < 8) {
            return "***";
        }
        return data.substring(0, 3) + "***" + data.substring(data.length() - 3);
    }
}