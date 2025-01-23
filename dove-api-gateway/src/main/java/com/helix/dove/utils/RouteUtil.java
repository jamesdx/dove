package com.helix.dove.utils;

import com.helix.dove.dto.RouteDTO;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.support.NotFoundException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

public class RouteUtil {
    
    public static RouteDefinition convertToDefinition(RouteDTO dto) {
        RouteDefinition definition = new RouteDefinition();
        definition.setId(dto.getId());
        definition.setUri(URI.create(dto.getUri()));
        definition.setPredicates(dto.getPredicates());
        definition.setFilters(dto.getFilters());
        definition.setMetadata(dto.getMetadata());
        definition.setOrder(dto.getOrder());
        return definition;
    }

    public static RouteDTO convertToDTO(Route route) {
        RouteDTO dto = new RouteDTO();
        dto.setId(route.getId());
        dto.setUri(route.getUri().toString());
        // TODO: Convert predicates and filters
        dto.setMetadata(route.getMetadata());
        dto.setOrder(route.getOrder());
        return dto;
    }

    public static Route.AsyncBuilder buildRoute(RouteLocatorBuilder.Builder builder, RouteDTO dto) {
        return builder.route(dto.getId(), r -> r.path("/**")
                .uri(dto.getUri()));
    }

    public static <T> Mono<T> throwNotFoundException(String message) {
        return Mono.error(new NotFoundException(message));
    }

    public static String generateRouteId() {
        return "route_" + System.currentTimeMillis();
    }

    public static boolean isValidUri(String uri) {
        try {
            URI.create(uri);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Map<String, Object> mergeMetadata(Map<String, Object> existing, Map<String, Object> update) {
        if (existing == null) {
            return update;
        }
        if (update != null) {
            existing.putAll(update);
        }
        return existing;
    }
}