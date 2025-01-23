package com.helix.dove.utils;

import com.helix.dove.dto.RouteDTO;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class PredicateUtil {
    
    public static List<PredicateDefinition> createPathPredicate(String pattern) {
        PredicateDefinition predicate = new PredicateDefinition();
        predicate.setName("Path");
        predicate.addArg("pattern", pattern);
        return List.of(predicate);
    }

    public static List<PredicateDefinition> createMethodPredicate(String method) {
        PredicateDefinition predicate = new PredicateDefinition();
        predicate.setName("Method");
        predicate.addArg("method", method);
        return List.of(predicate);
    }

    public static List<PredicateDefinition> createHostPredicate(String pattern) {
        PredicateDefinition predicate = new PredicateDefinition();
        predicate.setName("Host");
        predicate.addArg("pattern", pattern);
        return List.of(predicate);
    }

    public static List<PredicateDefinition> createHeaderPredicate(String header, String regexp) {
        PredicateDefinition predicate = new PredicateDefinition();
        predicate.setName("Header");
        predicate.addArg("header", header);
        predicate.addArg("regexp", regexp);
        return List.of(predicate);
    }

    public static List<PredicateDefinition> createTimePredicate(LocalTime start, LocalTime end, String zone) {
        PredicateDefinition predicate = new PredicateDefinition();
        predicate.setName("Between");
        predicate.addArg("start", start.toString());
        predicate.addArg("end", end.toString());
        predicate.addArg("zone", zone);
        return List.of(predicate);
    }

    public static Predicate<ServerWebExchange> combine(List<Predicate<ServerWebExchange>> predicates) {
        return exchange -> predicates.stream().allMatch(p -> p.test(exchange));
    }

    public static boolean isTimeInRange(LocalTime time, LocalTime start, LocalTime end, ZoneId zone) {
        LocalTime now = time.atDate(java.time.LocalDate.now()).atZone(zone).toLocalTime();
        if (start.isBefore(end)) {
            return !now.isBefore(start) && !now.isAfter(end);
        } else {
            return !now.isBefore(start) || !now.isAfter(end);
        }
    }

    public static List<PredicateDefinition> mergePredicates(List<PredicateDefinition> existing,
                                                          List<PredicateDefinition> update) {
        List<PredicateDefinition> merged = new ArrayList<>(existing);
        merged.addAll(update);
        return merged;
    }

    public static void validatePredicate(PredicateDefinition predicate) {
        if (predicate.getName() == null || predicate.getName().isEmpty()) {
            throw new IllegalArgumentException("Predicate name cannot be empty");
        }
        // Add more validation rules as needed
    }
}