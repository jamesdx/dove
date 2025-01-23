package com.helix.dove.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Table("audit_log")
public class AuditEntity {
    @Id
    private Long id;
    
    @Column("operation")
    private String operation;
    
    @Column("entity_type")
    private String entityType;
    
    @Column("entity_id")
    private Long entityId;
    
    @Column("user_id")
    private String userId;
    
    @Column("old_value")
    private Map<String, Object> oldValue;
    
    @Column("new_value")
    private Map<String, Object> newValue;
    
    @Column("created_time")
    private LocalDateTime createdTime;
}