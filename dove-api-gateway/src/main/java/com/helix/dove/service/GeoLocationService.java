package com.helix.dove.service;

import com.helix.dove.model.GeoCoordinate;

/**
 * Service interface for geolocation-related operations.
 */
public interface GeoLocationService {
    /**
     * Get the region code for an IP address.
     *
     * @param ipAddress the IP address to look up
     * @return the region code (e.g., "US", "EU", etc.) or null if unknown
     */
    String getRegion(String ipAddress);

    /**
     * Get the coordinates for an IP address.
     *
     * @param ipAddress the IP address to look up
     * @return the geographical coordinates
     */
    GeoCoordinate getCoordinates(String ipAddress);

    /**
     * Get the state/province for an IP address.
     *
     * @param ipAddress the IP address to look up
     * @return the state/province code or null if unknown
     */
    String getState(String ipAddress);

    /**
     * Check if an IP address is private.
     *
     * @param ipAddress the IP address to check
     * @return true if the IP is private, false otherwise
     */
    boolean isPrivateIP(String ipAddress);
} 