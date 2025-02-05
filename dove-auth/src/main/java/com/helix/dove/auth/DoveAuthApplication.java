package com.helix.dove.auth;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(
    info = @Info(
        title = "Dove Authentication Service",
        description = "Authentication and Authorization Service for Dove Platform",
        version = "1.0.0"
    )
)
public class DoveAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoveAuthApplication.class, args);
    }
} 