package com.helix.dove.config.predicates;

import lombok.Data;
import lombok.Setter;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.zone.ZoneRulesException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Route predicate factory that filters requests based on time windows.
 */
@Component
public class TimeBasedRoutePredicateFactory extends AbstractRoutePredicateFactory<TimeBasedRoutePredicateFactory.Config> {

    @Setter
    private Clock clock = Clock.systemUTC();

    @Setter
    private HolidayCalendar holidayCalendar;

    public TimeBasedRoutePredicateFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("startTime", "endTime", "timeZone", "holidayCalendar");
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        validateConfig(config);
        
        return exchange -> {
            if (clock == null) {
                clock = Clock.systemUTC();
            }
            
            ZoneId zoneId = config.getTimeZone() != null ? 
                ZoneId.of(config.getTimeZone()) : 
                ZoneId.systemDefault();

            Clock zonedClock = clock.withZone(zoneId);
            LocalTime now = LocalTime.now(zonedClock);
            LocalDate today = LocalDate.now(zonedClock);
            LocalTime start = LocalTime.parse(config.getStartTime(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime end = LocalTime.parse(config.getEndTime(), DateTimeFormatter.ofPattern("HH:mm"));

            // Check holiday calendar if configured
            if (holidayCalendar != null && holidayCalendar.isHoliday(today)) {
                return false;
            }

            if (end.isBefore(start)) {
                // Handle overnight time ranges
                return !now.isBefore(start) || !now.isAfter(end);
            } else {
                return !now.isBefore(start) && !now.isAfter(end);
            }
        };
    }

    private void validateConfig(Config config) {
        if (config.getStartTime() == null || config.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time must be specified");
        }
        
        try {
            parseTime(config.getStartTime());
            parseTime(config.getEndTime());
            getZoneId(config);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format. Use HH:mm format.", e);
        } catch (ZoneRulesException e) {
            throw new IllegalArgumentException("Invalid time zone", e);
        }
    }

    private LocalTime parseTime(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
    }

    private ZoneId getZoneId(Config config) {
        return config.getTimeZone() != null ? 
            ZoneId.of(config.getTimeZone()) : 
            ZoneId.systemDefault();
    }

    // For testing purposes
    void setClock(Clock clock) {
        this.clock = clock;
    }

    /**
     * Configuration class for TimeBasedRoutePredicateFactory.
     */
    @Data
    public static class Config {
        /**
         * Start time in HH:mm format.
         */
        private String startTime;

        /**
         * End time in HH:mm format.
         */
        private String endTime;

        /**
         * Time zone ID (optional).
         */
        private String timeZone;

        /**
         * Holiday calendar (optional).
         */
        private HolidayCalendar holidayCalendar;
    }
}