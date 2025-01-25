package com.helix.dove.auth.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuator.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MetricsConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.cloud.nacos.discovery.metadata.region:default}")
    private String region;

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config()
            .commonTags(Tags.of(
                Tag.of("application", applicationName),
                Tag.of("region", region)
            ));
    }

    @Bean
    public MeterBinder authMetrics(MeterRegistry registry) {
        return registry1 -> {
            // 登录成功率
            registry.gauge("auth.login.success.rate", 0.0);
            
            // 登录延迟
            registry.timer("auth.login.latency", 
                Tags.empty(), 
                TimeUnit.MILLISECONDS.toNanos(500));
            
            // 并发用户数
            registry.gauge("auth.concurrent.users", 0L);
            
            // MFA验证成功率
            registry.gauge("auth.mfa.success.rate", 0.0);
            
            // 密码重置请求数
            registry.counter("auth.password.reset.requests");
            
            // 社交登录成功率
            registry.gauge("auth.social.login.success.rate", 0.0);
            
            // API请求总数
            registry.counter("auth.api.requests.total");
            
            // API错误率
            registry.gauge("auth.api.error.rate", 0.0);
            
            // 会话数量
            registry.gauge("auth.sessions.active", 0L);
            
            // 限流触发次数
            registry.counter("auth.rate.limit.triggers");
            
            // 熔断触发次数
            registry.counter("auth.circuit.breaker.triggers");
            
            // 区域故障转移次数
            registry.counter("auth.region.failover");
        };
    }
} 