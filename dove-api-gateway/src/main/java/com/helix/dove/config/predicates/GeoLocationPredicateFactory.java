package com.helix.dove.config.predicates;

import com.helix.dove.model.GeoCoordinate;
import com.helix.dove.model.GeographyRule;
import com.helix.dove.service.GeoLocationService;
import lombok.Data;
import lombok.Setter;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Route predicate factory that filters requests based on geographic location.
 */
@Component
public class GeoLocationPredicateFactory extends AbstractRoutePredicateFactory<GeoLocationPredicateFactory.Config> {

    @Setter
    private GeoLocationService geoLocationService;

    public GeoLocationPredicateFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("allowedRegions", "latitude", "longitude", "radiusKm", "customRules");
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return exchange -> {
            if (geoLocationService == null) {
                return false;
            }

            InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
            if (remoteAddress == null) {
                return false;
            }

            InetAddress address = remoteAddress.getAddress();
            if (address == null) {
                return false;
            }

            String ipAddress = address.getHostAddress();
            if (ipAddress == null) {
                return false;
            }

            // Check for private IP addresses
            if (config.getAllowedRegions() != null && 
                config.getAllowedRegions().contains("PRIVATE") && 
                geoLocationService.isPrivateIP(ipAddress)) {
                return true;
            }

            // Region-based routing
            if (config.getAllowedRegions() != null && !config.getAllowedRegions().isEmpty()) {
                String region = geoLocationService.getRegion(ipAddress);
                if (region == null) {
                    return false;
                }
                
                // Check custom geography rules
                if (config.getCustomRules() != null && !config.getCustomRules().isEmpty()) {
                    String state = geoLocationService.getState(ipAddress);
                    if (state != null) {
                        for (GeographyRule rule : config.getCustomRules()) {
                            if (rule != null && 
                                rule.getRegionCode() != null && 
                                rule.getStates() != null &&
                                config.getAllowedRegions().contains(rule.getRegionCode()) &&
                                rule.getStates().contains(state)) {
                                return true;
                            }
                        }
                    }
                }
                
                return config.getAllowedRegions().contains(region);
            }

            // Coordinate-based routing
            if (config.getLatitude() != null && config.getLongitude() != null && config.getRadiusKm() != null) {
                GeoCoordinate location = geoLocationService.getCoordinates(ipAddress);
                if (location == null) {
                    return false;
                }
                
                double distance = calculateDistance(
                    config.getLatitude(), config.getLongitude(),
                    location.getLatitude(), location.getLongitude()
                );
                
                return distance <= config.getRadiusKm();
            }

            return false;
        };
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }

    /**
     * Configuration class for GeoLocationPredicateFactory.
     */
    @Data
    public static class Config {
        /**
         * List of allowed region codes.
         */
        private List<String> allowedRegions = new ArrayList<>();

        /**
         * Target latitude for coordinate-based routing.
         */
        private Double latitude;

        /**
         * Target longitude for coordinate-based routing.
         */
        private Double longitude;

        /**
         * Maximum allowed distance in kilometers.
         */
        private Double radiusKm;

        /**
         * Custom geography rules for fine-grained control.
         */
        private List<GeographyRule> customRules = new ArrayList<>();
    }
}