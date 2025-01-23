package com.helix.dove.utils;

import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.RewritePathGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.StripPrefixGatewayFilterFactory;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilterUtil {
    
    public static FilterDefinition createRewritePathFilter(String regex, String replacement) {
        FilterDefinition filter = new FilterDefinition();
        filter.setName("RewritePath");
        filter.addArg("regexp", regex);
        filter.addArg("replacement", replacement);
        return filter;
    }

    public static FilterDefinition createStripPrefixFilter(int parts) {
        FilterDefinition filter = new FilterDefinition();
        filter.setName("StripPrefix");
        filter.addArg("parts", String.valueOf(parts));
        return filter;
    }

    public static FilterDefinition createAddRequestHeaderFilter(String name, String value) {
        FilterDefinition filter = new FilterDefinition();
        filter.setName("AddRequestHeader");
        filter.addArg("name", name);
        filter.addArg("value", value);
        return filter;
    }

    public static FilterDefinition createAddResponseHeaderFilter(String name, String value) {
        FilterDefinition filter = new FilterDefinition();
        filter.setName("AddResponseHeader");
        filter.addArg("name", name);
        filter.addArg("value", value);
        return filter;
    }

    public static FilterDefinition createSetStatusFilter(HttpStatus status) {
        FilterDefinition filter = new FilterDefinition();
        filter.setName("SetStatus");
        filter.addArg("status", String.valueOf(status.value()));
        return filter;
    }

    public static FilterDefinition createRetryFilter(int retries, List<HttpStatus> statuses) {
        FilterDefinition filter = new FilterDefinition();
        filter.setName("Retry");
        filter.addArg("retries", String.valueOf(retries));
        filter.addArg("statuses", statuses.stream()
                .map(HttpStatus::name)
                .reduce((a, b) -> a + "," + b)
                .orElse(""));
        return filter;
    }

    public static List<FilterDefinition> mergeFilters(List<FilterDefinition> existing,
                                                    List<FilterDefinition> update) {
        List<FilterDefinition> merged = new ArrayList<>(existing);
        merged.addAll(update);
        return merged;
    }

    public static void validateFilter(FilterDefinition filter) {
        if (filter.getName() == null || filter.getName().isEmpty()) {
            throw new IllegalArgumentException("Filter name cannot be empty");
        }
        // Add more validation rules as needed
    }

    public static GatewayFilter createRewritePathGatewayFilter(String regex, String replacement) {
        RewritePathGatewayFilterFactory.Config config = new RewritePathGatewayFilterFactory.Config();
        config.setRegexp(regex);
        config.setReplacement(replacement);
        return new RewritePathGatewayFilterFactory().apply(config);
    }

    public static GatewayFilter createStripPrefixGatewayFilter(int parts) {
        StripPrefixGatewayFilterFactory.Config config = new StripPrefixGatewayFilterFactory.Config();
        config.setParts(parts);
        return new StripPrefixGatewayFilterFactory().apply(config);
    }
}