package com.helix.dove.repository;

import com.helix.dove.entity.RouteEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RouteRepository extends ReactiveCrudRepository<RouteEntity, Long> {
    Flux<RouteEntity> findByServiceName(String serviceName);
    Flux<RouteEntity> findByEnabled(Boolean enabled);
    Mono<RouteEntity> findByRouteId(String routeId);
    Flux<RouteEntity> findByPathLike(String pathPattern);
    Flux<RouteEntity> findByHostLike(String hostPattern);
}