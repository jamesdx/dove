package com.helix.dove.service;

import com.helix.dove.entity.AuditEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuditService {
    Mono<AuditEntity> createAudit(String operation, String entityType, Long entityId, 
                                 Object oldValue, Object newValue);
    Flux<AuditEntity> getAuditsByEntityTypeAndId(String entityType, Long entityId);
    Flux<AuditEntity> getAuditsByUserId(String userId);
    Flux<AuditEntity> getAuditsByOperation(String operation);
    Mono<Void> clearAudits();
}