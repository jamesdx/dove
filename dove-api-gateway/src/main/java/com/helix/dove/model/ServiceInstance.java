package com.helix.dove.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a service instance in the system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceInstance {
    /**
     * The service ID this instance belongs to.
     */
    private String serviceId;

    /**
     * The host name or IP address of this instance.
     */
    private String host;

    /**
     * The port number this instance is listening on.
     */
    private int port;

    /**
     * Get the URI of this instance.
     *
     * @return the URI in the format host:port
     */
    public String getUri() {
        return host + ":" + port;
    }
} 