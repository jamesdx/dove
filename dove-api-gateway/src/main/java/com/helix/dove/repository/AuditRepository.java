package com.helix.dove.repository;

import com.helix.dove.entity.AuditEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AuditRepository extends ReactiveCrudRepository<AuditEntity, Long> {
    Flux<AuditEntity> findByEntityTypeAndEntityId(String entityType, Long entityId);
    Flux<AuditEntity> findByUserId(String userId);
    Flux<AuditEntity> findByOperation(String operation);
}