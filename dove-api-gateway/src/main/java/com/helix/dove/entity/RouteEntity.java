package com.helix.dove.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Table("route_config")
public class RouteEntity {
    @Id
    private Long id;
    private String routeId;
    private String serviceName;
    private String path;
    private String host;
    private Map<String, String> headers;
    private Map<String, String> queryParams;
    private String methods;
    private String uri;
    private String predicates;
    private String filters;
    private Integer order;
    private Map<String, Object> metadata;
    private Boolean enabled;
    private Integer weight;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}