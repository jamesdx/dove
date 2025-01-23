package com.helix.dove.repository;

import com.helix.dove.entity.FilterEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface FilterRepository extends ReactiveCrudRepository<FilterEntity, Long> {
    Flux<FilterEntity> findByRouteId(Long routeId);
    Flux<FilterEntity> findByRouteIdAndEnabled(Long routeId, Boolean enabled);
    Flux<FilterEntity> findByName(String name);
}