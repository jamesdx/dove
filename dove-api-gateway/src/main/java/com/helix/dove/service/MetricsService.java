package com.helix.dove.service;

import com.helix.dove.dto.MetricsDTO;
import reactor.core.publisher.Mono;

public interface MetricsService {
    Mono<MetricsDTO> getMetrics();
    Mono<MetricsDTO> getRouteMetrics();
    Mono<MetricsDTO> getSystemMetrics();
    Mono<Void> recordRouteMetric(String routeId, String metricName, double value);
    Mono<Void> recordSystemMetric(String metricName, double value);
    Mono<Void> clearMetrics();
}