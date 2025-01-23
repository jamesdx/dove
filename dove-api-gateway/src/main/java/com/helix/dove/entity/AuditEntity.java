package com.helix.dove.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Table("route_audit_log")
public class AuditEntity {
    @Id
    private Long id;
    private String operation;
    private String entityType;
    private Long entityId;
    private String userId;
    private String userIp;
    private Map<String, Object> oldValue;
    private Map<String, Object> newValue;
    private LocalDateTime createdTime;
}