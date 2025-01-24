package com.helix.dove.config.predicates;

import com.helix.dove.model.GeoCoordinate;
import com.helix.dove.model.GeographyRule;
import com.helix.dove.service.GeoLocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Geo Location Predicate Factory Tests")
class GeoLocationPredicateFactoryTest {

    private GeoLocationPredicateFactory predicateFactory;
    
    @Mock
    private GeoLocationService geoLocationService;

    @BeforeEach
    void setUp() {
        predicateFactory = new GeoLocationPredicateFactory();
        predicateFactory.setGeoLocationService(geoLocationService);
    }

    @Nested
    @DisplayName("Region Based Routing Tests")
    class RegionBasedRoutingTests {

        @Test
        @DisplayName("Should allow request from allowed region")
        void shouldAllowRequestFromAllowedRegion() {
            // Arrange
            GeoLocationPredicateFactory.Config config = new GeoLocationPredicateFactory.Config();
            config.setAllowedRegions(Arrays.asList("US", "EU"));
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                    .remoteAddress(new InetSocketAddress("192.168.1.1", 8080))
                    .build()
            );
            
            when(geoLocationService.getRegion("192.168.1.1")).thenReturn("US");
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isTrue();
        }

        @Test
        @DisplayName("Should reject request from disallowed region")
        void shouldRejectRequestFromDisallowedRegion() {
            // Arrange
            GeoLocationPredicateFactory.Config config = new GeoLocationPredicateFactory.Config();
            config.setAllowedRegions(Arrays.asList("US", "EU"));
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                    .remoteAddress(new InetSocketAddress("192.168.1.2", 8080))
                    .build()
            );
            
            when(geoLocationService.getRegion("192.168.1.2")).thenReturn("CN");
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isFalse();
        }
    }

    @Nested
    @DisplayName("Coordinate Based Tests")
    class CoordinateBasedTests {

        @Test
        @DisplayName("Should allow request within radius")
        void shouldAllowRequestWithinRadius() {
            // Arrange
            GeoLocationPredicateFactory.Config config = new GeoLocationPredicateFactory.Config();
            config.setLatitude(40.7128);
            config.setLongitude(-74.0060);
            config.setRadiusKm(10.0);
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                    .remoteAddress(new InetSocketAddress("192.168.1.3", 8080))
                    .build()
            );
            
            when(geoLocationService.getCoordinates("192.168.1.3"))
                .thenReturn(new GeoCoordinate(40.7200, -74.0100));
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isTrue();
        }

        @Test
        @DisplayName("Should reject request outside radius")
        void shouldRejectRequestOutsideRadius() {
            // Arrange
            GeoLocationPredicateFactory.Config config = new GeoLocationPredicateFactory.Config();
            config.setLatitude(40.7128);
            config.setLongitude(-74.0060);
            config.setRadiusKm(10.0);
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                    .remoteAddress(new InetSocketAddress("192.168.1.4", 8080))
                    .build()
            );
            
            when(geoLocationService.getCoordinates("192.168.1.4"))
                .thenReturn(new GeoCoordinate(41.8781, -87.6298)); // Chicago coordinates
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isFalse();
        }
    }

    @Nested
    @DisplayName("Custom Geography Rule Tests")
    class CustomGeographyRuleTests {

        @Test
        @DisplayName("Should apply custom geography rules correctly")
        void shouldApplyCustomGeographyRulesCorrectly() {
            // Arrange
            GeoLocationPredicateFactory.Config config = new GeoLocationPredicateFactory.Config();
            config.setAllowedRegions(Arrays.asList("US-EAST"));
            config.setCustomRules(Arrays.asList(
                new GeographyRule("US-EAST", Arrays.asList("NY", "NJ", "CT")),
                new GeographyRule("US-WEST", Arrays.asList("CA", "WA", "OR"))
            ));
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                    .remoteAddress(new InetSocketAddress("192.168.1.5", 8080))
                    .build()
            );
            
            when(geoLocationService.getRegion("192.168.1.5")).thenReturn("US-EAST");
            when(geoLocationService.getState("192.168.1.5")).thenReturn("NY");
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isTrue();
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle unknown IP addresses gracefully")
        void shouldHandleUnknownIPAddressesGracefully() {
            // Arrange
            GeoLocationPredicateFactory.Config config = new GeoLocationPredicateFactory.Config();
            config.setAllowedRegions(Arrays.asList("US", "EU"));
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                    .remoteAddress(null)
                    .build()
            );
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isFalse();
        }

        @Test
        @DisplayName("Should handle private IP addresses correctly")
        void shouldHandlePrivateIPAddressesCorrectly() {
            // Arrange
            GeoLocationPredicateFactory.Config config = new GeoLocationPredicateFactory.Config();
            config.setAllowedRegions(Arrays.asList("PRIVATE"));
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                    .remoteAddress(new InetSocketAddress("10.0.0.1", 8080))
                    .build()
            );
            
            when(geoLocationService.isPrivateIP("10.0.0.1")).thenReturn(true);
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isTrue();
        }
    }
} 