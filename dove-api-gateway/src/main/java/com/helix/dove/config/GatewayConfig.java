package com.helix.dove.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;

import java.time.Duration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // Default fallback route
            .route("default_fallback_route", r -> r
                .path("/fallback")
                .uri("lb://fallback-service"))
            // Example path-based route
            .route("path_route", r -> r
                .path("/api/**")
                .uri("lb://api-service"))
            // Example host-based route
            .route("host_route", r -> r
                .host("api.example.com")
                .uri("lb://host-service"))
            .build();
    }

    @Bean
    public Cache<String, Object> routeCache() {
        return Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(10))
            .build();
    }

    @Bean
    public ReactiveRedisTemplate<String, Object> redisTemplate(ReactiveRedisConnectionFactory factory) {
        RedisSerializationContext<String, Object> context = RedisSerializationContext
            .<String, Object>newSerializationContext()
            .key(RedisSerializer.string())
            .value(RedisSerializer.json())
            .hashKey(RedisSerializer.string())
            .hashValue(RedisSerializer.json())
            .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    public CircuitBreakerConfig circuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
            .failureRateThreshold(50)
            .waitDurationInOpenState(Duration.ofSeconds(60))
            .slidingWindowSize(100)
            .build();
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
            .circuitBreakerConfig(circuitBreakerConfig())
            .timeLimiterConfig(TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(5))
                .build())
            .build());
    }
}