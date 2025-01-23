package com.helix.dove.repository;

import com.helix.dove.entity.PredicateEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PredicateRepository extends ReactiveCrudRepository<PredicateEntity, Long> {
    Flux<PredicateEntity> findByRouteId(Long routeId);
    Flux<PredicateEntity> findByRouteIdAndEnabled(Long routeId, Boolean enabled);
    Flux<PredicateEntity> findByName(String name);
}