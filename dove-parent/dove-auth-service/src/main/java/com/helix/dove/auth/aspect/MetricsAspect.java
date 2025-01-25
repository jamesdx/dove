package com.helix.dove.auth.aspect;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MetricsAspect {

    private final MeterRegistry registry;

    @Around("execution(* com.helix.dove.auth.controller.*.*(..))")
    public Object recordMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Timer.Sample sample = Timer.start(registry);
        
        try {
            Object result = joinPoint.proceed();
            sample.stop(registry.timer("auth.method.latency", "method", methodName, "status", "success"));
            registry.counter("auth.method.calls", "method", methodName, "status", "success").increment();
            return result;
        } catch (Exception e) {
            sample.stop(registry.timer("auth.method.latency", "method", methodName, "status", "error"));
            registry.counter("auth.method.calls", "method", methodName, "status", "error").increment();
            throw e;
        }
    }

    @Around("execution(* com.helix.dove.auth.service.AuthService.login(..))")
    public Object recordLoginMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
        Timer.Sample sample = Timer.start(registry);
        
        try {
            Object result = joinPoint.proceed();
            sample.stop(registry.timer("auth.login.latency"));
            registry.counter("auth.login.success").increment();
            updateLoginSuccessRate();
            return result;
        } catch (Exception e) {
            sample.stop(registry.timer("auth.login.latency"));
            registry.counter("auth.login.failure").increment();
            updateLoginSuccessRate();
            throw e;
        }
    }

    private void updateLoginSuccessRate() {
        long success = registry.counter("auth.login.success").count();
        long failure = registry.counter("auth.login.failure").count();
        double total = success + failure;
        if (total > 0) {
            registry.gauge("auth.login.success.rate", success / total);
        }
    }
} 