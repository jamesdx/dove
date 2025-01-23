package com.helix.dove.service.impl;

import com.helix.dove.dto.MetricsDTO;
import com.helix.dove.service.MetricsService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MetricsServiceImpl implements MetricsService {

    private final MeterRegistry meterRegistry;
    private final Map<String, Timer> routeTimers = new ConcurrentHashMap<>();

    @Override
    public Mono<MetricsDTO> getMetrics() {
        MetricsDTO metrics = new MetricsDTO();
        metrics.getCounters().put("total_requests", meterRegistry.counter("gateway.requests").count());
        metrics.getCounters().put("total_errors", meterRegistry.counter("gateway.errors").count());
        metrics.getGauges().put("active_connections", meterRegistry.gauge("gateway.connections", 0.0));
        return Mono.just(metrics);
    }

    @Override
    public Mono<MetricsDTO> getRouteMetrics() {
        MetricsDTO metrics = new MetricsDTO();
        routeTimers.forEach((routeId, timer) -> {
            MetricsDTO.Timer timerMetrics = new MetricsDTO.Timer();
            timerMetrics.setCount(timer.count());
            timerMetrics.setTotalTime(timer.totalTime(TimeUnit.MILLISECONDS));
            timerMetrics.setMax(timer.max(TimeUnit.MILLISECONDS));
            timerMetrics.setMean(timer.mean(TimeUnit.MILLISECONDS));
            Map<Double, Double> percentiles = new HashMap<>();
            percentiles.put(0.5, timer.percentile(0.5, TimeUnit.MILLISECONDS));
            percentiles.put(0.95, timer.percentile(0.95, TimeUnit.MILLISECONDS));
            percentiles.put(0.99, timer.percentile(0.99, TimeUnit.MILLISECONDS));
            timerMetrics.setPercentiles(percentiles);
            metrics.getTimers().put(routeId, timerMetrics);
        });
        return Mono.just(metrics);
    }

    @Override
    public Mono<MetricsDTO> getSystemMetrics() {
        MetricsDTO metrics = new MetricsDTO();
        metrics.getGauges().put("cpu_usage", meterRegistry.gauge("system.cpu.usage", 0.0));
        metrics.getGauges().put("memory_used", meterRegistry.gauge("jvm.memory.used", 0.0));
        metrics.getGauges().put("memory_max", meterRegistry.gauge("jvm.memory.max", 0.0));
        return Mono.just(metrics);
    }

    @Override
    public Mono<Void> recordRouteMetric(String routeId, String metricName, double value) {
        meterRegistry.counter(metricName, "routeId", routeId).increment(value);
        return Mono.empty();
    }

    @Override
    public Mono<Void> recordSystemMetric(String metricName, double value) {
        meterRegistry.gauge(metricName, value);
        return Mono.empty();
    }

    @Override
    public Mono<Void> clearMetrics() {
        routeTimers.clear();
        return Mono.empty();
    }
}