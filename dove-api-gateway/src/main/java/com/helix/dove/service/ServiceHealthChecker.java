package com.helix.dove.service;

import com.helix.dove.model.ServiceInstance;
import com.helix.dove.model.HealthStatus;

/**
 * Interface for checking the health status of service instances.
 */
public interface ServiceHealthChecker {
    /**
     * Check the health status of a service instance.
     *
     * @param instance the service instance to check
     * @return the health status of the instance
     */
    HealthStatus checkHealth(ServiceInstance instance);
} 