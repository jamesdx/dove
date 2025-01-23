package com.helix.dove.controller;

import com.helix.dove.dto.ResponseDTO;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/actuator")
public class HealthController implements HealthIndicator {

    @Override
    public Health health() {
        return Health.up()
                .withDetail("app", "dove-api-gateway")
                .withDetail("description", "Spring Cloud Gateway")
                .build();
    }

    @GetMapping("/health/check")
    public Mono<ResponseDTO<Health>> check() {
        return Mono.just(ResponseDTO.success(health()));
    }
}