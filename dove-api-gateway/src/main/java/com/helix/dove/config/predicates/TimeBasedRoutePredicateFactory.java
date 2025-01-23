package com.helix.dove.config.predicates;

import lombok.Data;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class TimeBasedRoutePredicateFactory extends AbstractRoutePredicateFactory<TimeBasedRoutePredicateFactory.Config> {

    public TimeBasedRoutePredicateFactory() {
        super(Config.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return exchange -> {
            LocalTime now = LocalTime.now(ZoneId.of(config.getTimeZone()));
            return now.isAfter(config.getStartTime()) && now.isBefore(config.getEndTime());
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("startTime", "endTime", "timeZone");
    }

    @Data
    public static class Config {
        private LocalTime startTime = LocalTime.parse("00:00");
        private LocalTime endTime = LocalTime.parse("23:59");
        private String timeZone = "UTC";
    }
}