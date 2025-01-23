package com.helix.dove.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helix.dove.entity.AuditEntity;
import com.helix.dove.repository.AuditRepository;
import com.helix.dove.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<AuditEntity> createAudit(String operation, String entityType, Long entityId,
                                        Object oldValue, Object newValue) {
        AuditEntity audit = new AuditEntity();
        audit.setOperation(operation);
        audit.setEntityType(entityType);
        audit.setEntityId(entityId);
        audit.setOldValue(convertToMap(oldValue));
        audit.setNewValue(convertToMap(newValue));
        audit.setCreatedTime(LocalDateTime.now());
        return auditRepository.save(audit);
    }

    @Override
    public Flux<AuditEntity> getAuditsByEntityTypeAndId(String entityType, Long entityId) {
        return auditRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Override
    public Flux<AuditEntity> getAuditsByUserId(String userId) {
        return auditRepository.findByUserId(userId);
    }

    @Override
    public Flux<AuditEntity> getAuditsByOperation(String operation) {
        return auditRepository.findByOperation(operation);
    }

    @Override
    public Mono<Void> clearAudits() {
        return auditRepository.deleteAll();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> convertToMap(Object value) {
        if (value == null) {
            return null;
        }
        return objectMapper.convertValue(value, Map.class);
    }
}