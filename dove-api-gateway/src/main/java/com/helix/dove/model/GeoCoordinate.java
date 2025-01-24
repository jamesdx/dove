package com.helix.dove.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents geographical coordinates with latitude and longitude.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoCoordinate {
    /**
     * Latitude in decimal degrees.
     */
    private double latitude;

    /**
     * Longitude in decimal degrees.
     */
    private double longitude;
} 