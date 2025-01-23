package com.helix.dove.utils;

import com.helix.dove.dto.RouteDTO;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RouteUtil {
    
    public static Route convertToRoute(RouteDTO routeDTO) {
        return Route.async()
                .id(routeDTO.getId())
                .uri(routeDTO.getUri())
                .predicate(exchange -> true)
                .order(routeDTO.getOrder())
                .build();
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

    public static Route buildRoute(RouteLocatorBuilder.Builder builder, RouteDTO dto) {
        return builder.route(dto.getId(), r -> r.path("/**")
                .uri(dto.getUri()))
                .build()
                .getRoutes()
                .blockFirst();
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

    public static Map<String, String> mergeMetadata(Map<String, String> existing, Map<String, String> update) {
        if (existing == null) {
            return update;
        }
        if (update != null) {
            existing.putAll(update);
        }
        return existing;
    }

    private static List<PredicateDefinition> convertToPredicateDefinitions(List<RouteDTO.PredicateDTO> predicates) {
        return predicates.stream()
                .map(p -> {
                    PredicateDefinition pd = new PredicateDefinition();
                    pd.setName(p.getName());
                    pd.setArgs(p.getArgs().entrySet().stream()
                            .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> String.valueOf(e.getValue())
                            )));
                    return pd;
                })
                .collect(Collectors.toList());
    }

    private static List<FilterDefinition> convertToFilterDefinitions(List<RouteDTO.FilterDTO> filters) {
        return filters.stream()
                .map(f -> {
                    FilterDefinition fd = new FilterDefinition();
                    fd.setName(f.getName());
                    fd.setArgs(f.getArgs().entrySet().stream()
                            .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> String.valueOf(e.getValue())
                            )));
                    return fd;
                })
                .collect(Collectors.toList());
    }

    public static RouteLocator buildRouteLocator(RouteLocatorBuilder builder, List<RouteDTO> routes) {
        RouteLocatorBuilder.Builder routeBuilder = builder.routes();
        routes.forEach(route -> 
            routeBuilder.route(route.getId(), 
                r -> r.predicate(exchange -> true)
                    .uri(route.getUri())
            )
        );
        return routeBuilder.build();
    }
}