package com.helix.dove.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RouteDTO {
    private String id;
    private String uri;
    private List<PredicateDTO> predicates = new ArrayList<>();
    private List<FilterDTO> filters = new ArrayList<>();
    private Map<String, Object> metadata = new HashMap<>();
    private int order;
    private boolean enabled = true;

    @Data
    public static class PredicateDTO {
        private String name;
        private Map<String, Object> args = new HashMap<>();
    }

    @Data
    public static class FilterDTO {
        private String name;
        private Map<String, Object> args = new HashMap<>();
    }
}