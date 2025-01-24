package com.helix.dove.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a geographical routing rule.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeographyRule {
    /**
     * The region code for this rule (e.g., "US-EAST", "US-WEST").
     */
    private String regionCode;

    /**
     * List of state/province codes included in this region.
     */
    private List<String> states;
} 