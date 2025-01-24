package com.helix.dove.config.predicates;

import com.helix.dove.model.HealthStatus;
import com.helix.dove.model.ServiceInstance;
import com.helix.dove.service.LoadBalanceService;
import com.helix.dove.service.ServiceHealthChecker;
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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Load Balance Predicate Factory Tests")
class LoadBalancePredicateFactoryTest {

    private LoadBalancePredicateFactory predicateFactory;
    
    @Mock
    private LoadBalanceService loadBalanceService;
    
    @Mock
    private ServiceHealthChecker healthChecker;

    @BeforeEach
    void setUp() {
        predicateFactory = new LoadBalancePredicateFactory();
        predicateFactory.setLoadBalanceService(loadBalanceService);
        predicateFactory.setHealthChecker(healthChecker);
    }

    @Nested
    @DisplayName("Load-Based Routing Tests")
    class LoadBasedRoutingTests {

        @Test
        @DisplayName("Should route to instance with acceptable load")
        void shouldRouteToInstanceWithAcceptableLoad() {
            // Arrange
            LoadBalancePredicateFactory.Config config = new LoadBalancePredicateFactory.Config();
            config.setMaxLoad(80.0);
            config.setServiceId("test-service");
            
            ServiceInstance instance = new ServiceInstance("test-service", "localhost", 8080);
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").build()
            );
            
            when(loadBalanceService.getInstanceLoad(instance)).thenReturn(70.0);
            when(loadBalanceService.selectInstance(eq("test-service"), any())).thenReturn(instance);
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isTrue();
        }

        @Test
        @DisplayName("Should reject routing when load is too high")
        void shouldRejectRoutingWhenLoadIsTooHigh() {
            // Arrange
            LoadBalancePredicateFactory.Config config = new LoadBalancePredicateFactory.Config();
            config.setMaxLoad(80.0);
            config.setServiceId("test-service");
            
            ServiceInstance instance = new ServiceInstance("test-service", "localhost", 8080);
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").build()
            );
            
            when(loadBalanceService.getInstanceLoad(instance)).thenReturn(90.0);
            when(loadBalanceService.selectInstance(eq("test-service"), any())).thenReturn(instance);
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isFalse();
        }
    }

    @Nested
    @DisplayName("Resource Metrics Tests")
    class ResourceMetricsTests {

        @Test
        @DisplayName("Should consider multiple resource metrics")
        void shouldConsiderMultipleResourceMetrics() {
            // Arrange
            LoadBalancePredicateFactory.Config config = new LoadBalancePredicateFactory.Config();
            config.setServiceId("test-service");
            Map<String, Double> thresholds = new HashMap<>();
            thresholds.put("cpu", 80.0);
            thresholds.put("memory", 75.0);
            config.setResourceThresholds(thresholds);
            
            ServiceInstance instance = new ServiceInstance("test-service", "localhost", 8080);
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").build()
            );
            
            Map<String, Double> metrics = new HashMap<>();
            metrics.put("cpu", 70.0);
            metrics.put("memory", 60.0);
            
            when(loadBalanceService.getResourceMetrics(instance)).thenReturn(metrics);
            when(loadBalanceService.selectInstance(eq("test-service"), any())).thenReturn(instance);
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isTrue();
        }
    }

    @Nested
    @DisplayName("Health Check Tests")
    class HealthCheckTests {

        @Test
        @DisplayName("Should check instance health before routing")
        void shouldCheckInstanceHealthBeforeRouting() {
            // Arrange
            LoadBalancePredicateFactory.Config config = new LoadBalancePredicateFactory.Config();
            config.setServiceId("test-service");
            config.setRequireHealthCheck(true);
            
            ServiceInstance instance = new ServiceInstance("test-service", "localhost", 8080);
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").build()
            );
            
            when(loadBalanceService.selectInstance(eq("test-service"), any())).thenReturn(instance);
            when(healthChecker.checkHealth(instance)).thenReturn(HealthStatus.UP);
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isTrue();
        }

        @Test
        @DisplayName("Should reject unhealthy instance")
        void shouldRejectUnhealthyInstance() {
            // Arrange
            LoadBalancePredicateFactory.Config config = new LoadBalancePredicateFactory.Config();
            config.setServiceId("test-service");
            config.setRequireHealthCheck(true);
            
            ServiceInstance instance = new ServiceInstance("test-service", "localhost", 8080);
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").build()
            );
            
            when(loadBalanceService.selectInstance(eq("test-service"), any())).thenReturn(instance);
            when(healthChecker.checkHealth(instance)).thenReturn(HealthStatus.DOWN);
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isFalse();
        }
    }

    @Nested
    @DisplayName("Weighted Routing Tests")
    class WeightedRoutingTests {

        @Test
        @DisplayName("Should route based on instance weights")
        void shouldRouteBasedOnInstanceWeights() {
            // Arrange
            LoadBalancePredicateFactory.Config config = new LoadBalancePredicateFactory.Config();
            config.setServiceId("test-service");
            Map<String, Integer> weights = new HashMap<>();
            weights.put("instance-1", 80);
            weights.put("instance-2", 20);
            config.setInstanceWeights(weights);
            
            ServiceInstance instance = new ServiceInstance("test-service", "localhost", 8080);
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").build()
            );
            
            when(loadBalanceService.selectWeightedInstance(eq("test-service"), eq(weights)))
                .thenReturn(instance);
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isTrue();
        }
    }
} 