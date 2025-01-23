package com.helix.dove.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class MetricsConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config()
                .commonTags(Arrays.asList(
                        Tag.of("application", applicationName),
                        Tag.of("env", "${spring.profiles.active:dev}")
                ))
                .meterFilter(MeterFilterConfig.denyNameStartsWith("jvm"))
                .meterFilter(MeterFilterConfig.denyNameStartsWith("system"))
                .meterFilter(MeterFilterConfig.maximumAllowableMetrics(100));
    }
}