package com.helix.dove.config.predicates;

import com.helix.dove.model.HealthStatus;
import com.helix.dove.model.ServiceInstance;
import com.helix.dove.service.LoadBalanceService;
import com.helix.dove.service.ServiceHealthChecker;
import lombok.Data;
import lombok.Setter;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Route predicate factory that filters requests based on load balancing rules.
 */
@Component
public class LoadBalancePredicateFactory extends AbstractRoutePredicateFactory<LoadBalancePredicateFactory.Config> {

    @Setter
    private LoadBalanceService loadBalanceService;

    @Setter
    private ServiceHealthChecker healthChecker;

    public LoadBalancePredicateFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("serviceId", "maxLoad", "resourceThresholds", "requireHealthCheck", "instanceWeights");
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return exchange -> {
            if (loadBalanceService == null) {
                return false;
            }

            ServiceInstance instance;
            if (config.getInstanceWeights() != null && !config.getInstanceWeights().isEmpty()) {
                instance = loadBalanceService.selectWeightedInstance(config.getServiceId(), config.getInstanceWeights());
            } else {
                instance = loadBalanceService.selectInstance(config.getServiceId(), "LEAST_LOAD");
            }

            if (instance == null) {
                return false;
            }

            // Check health if required
            if (config.isRequireHealthCheck() && healthChecker != null) {
                if (healthChecker.checkHealth(instance) != HealthStatus.UP) {
                    return false;
                }
            }

            // Check load threshold
            if (config.getMaxLoad() != null) {
                double load = loadBalanceService.getInstanceLoad(instance);
                if (load > config.getMaxLoad()) {
                    return false;
                }
            }

            // Check resource thresholds
            if (config.getResourceThresholds() != null && !config.getResourceThresholds().isEmpty()) {
                Map<String, Double> metrics = loadBalanceService.getResourceMetrics(instance);
                for (Map.Entry<String, Double> threshold : config.getResourceThresholds().entrySet()) {
                    Double metric = metrics.get(threshold.getKey());
                    if (metric == null || metric > threshold.getValue()) {
                        return false;
                    }
                }
            }

            return true;
        };
    }

    /**
     * Configuration class for LoadBalancePredicateFactory.
     */
    @Data
    public static class Config {
        /**
         * The service ID to load balance.
         */
        private String serviceId;

        /**
         * Maximum allowed load percentage.
         */
        private Double maxLoad;

        /**
         * Resource threshold map (resource name -> threshold).
         */
        private Map<String, Double> resourceThresholds;

        /**
         * Whether to require health check.
         */
        private boolean requireHealthCheck;

        /**
         * Instance weights for weighted load balancing.
         */
        private Map<String, Integer> instanceWeights;
    }
}