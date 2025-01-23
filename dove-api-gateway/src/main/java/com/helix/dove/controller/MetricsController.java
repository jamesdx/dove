package com.helix.dove.controller;

import com.helix.dove.dto.MetricsDTO;
import com.helix.dove.dto.ResponseDTO;
import com.helix.dove.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricsService metricsService;

    @GetMapping
    public Mono<ResponseDTO<MetricsDTO>> getMetrics() {
        return metricsService.getMetrics()
                .map(ResponseDTO::success);
    }

    @GetMapping("/routes")
    public Mono<ResponseDTO<MetricsDTO>> getRouteMetrics() {
        return metricsService.getRouteMetrics()
                .map(ResponseDTO::success);
    }

    @GetMapping("/system")
    public Mono<ResponseDTO<MetricsDTO>> getSystemMetrics() {
        return metricsService.getSystemMetrics()
                .map(ResponseDTO::success);
    }
}