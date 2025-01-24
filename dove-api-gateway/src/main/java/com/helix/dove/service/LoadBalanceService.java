package com.helix.dove.service;

import com.helix.dove.model.ServiceInstance;

import java.util.Map;

/**
 * Service interface for load balancing operations.
 */
public interface LoadBalanceService {
    /**
     * Get the current load of a service instance.
     *
     * @param instance the service instance to check
     * @return the load percentage (0-100)
     */
    double getInstanceLoad(ServiceInstance instance);

    /**
     * Get various resource metrics for a service instance.
     *
     * @param instance the service instance to check
     * @return map of metric name to value
     */
    Map<String, Double> getResourceMetrics(ServiceInstance instance);

    /**
     * Select an instance based on the current strategy.
     *
     * @param serviceId the service ID
     * @param strategy the load balancing strategy
     * @return the selected instance or null if none available
     */
    ServiceInstance selectInstance(String serviceId, String strategy);

    /**
     * Select an instance based on weights.
     *
     * @param serviceId the service ID
     * @param weights map of instance IDs to their weights
     * @return the selected instance or null if none available
     */
    ServiceInstance selectWeightedInstance(String serviceId, Map<String, Integer> weights);
} 