package com.helix.dove.service;

import com.helix.dove.dto.RouteDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RouteService {
    Mono<RouteDTO> createRoute(RouteDTO route);
    Mono<RouteDTO> updateRoute(String id, RouteDTO route);
    Mono<Void> deleteRoute(String id);
    Mono<RouteDTO> getRoute(String id);
    Flux<RouteDTO> getRoutes();
    Flux<RouteDTO> getRoutesByServiceName(String serviceName);
    Flux<RouteDTO> getRoutesByEnabled(Boolean enabled);
    Mono<RouteDTO> updateRouteStatus(String id, Boolean enabled);
    Mono<RouteDTO> updateRouteWeight(String id, Integer weight);
    Mono<RouteDTO> addPredicate(String id, Object predicate);
    Mono<RouteDTO> addFilter(String id, Object filter);
    Mono<Void> deletePredicate(String id, String predicateId);
    Mono<Void> deleteFilter(String id, String filterId);
}