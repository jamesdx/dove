package com.helix.dove.model;

/**
 * Enum representing the health status of a service instance.
 */
public enum HealthStatus {
    /**
     * Service is healthy and ready to accept requests.
     */
    UP,

    /**
     * Service is unhealthy and should not receive requests.
     */
    DOWN,

    /**
     * Service health status is unknown.
     */
    UNKNOWN,

    /**
     * Service is starting up but not ready yet.
     */
    STARTING,

    /**
     * Service is shutting down.
     */
    STOPPING,

    /**
     * Service is out of service for maintenance.
     */
    OUT_OF_SERVICE
} 