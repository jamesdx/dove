package com.helix.dove.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Table("route_filter")
public class FilterEntity {
    @Id
    private Long id;
    private Long routeId;
    private String name;
    private Map<String, Object> args;
    private Integer order;
    private Boolean enabled;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}