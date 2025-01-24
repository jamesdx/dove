package com.helix.dove.config;

import com.helix.dove.config.predicates.DefaultHolidayCalendar;
import com.helix.dove.config.predicates.HolidayCalendar;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Test configuration class that provides test-specific beans.
 */
@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public HolidayCalendar testHolidayCalendar() {
        return new DefaultHolidayCalendar();
    }
} 