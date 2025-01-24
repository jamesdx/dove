package com.helix.dove.config.predicates;

import com.helix.dove.service.BusinessRuleEngine;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Business Rule Predicate Factory Tests")
class BusinessRulePredicateFactoryTest {

    private BusinessRulePredicateFactory predicateFactory;
    
    @Mock
    private BusinessRuleEngine ruleEngine;

    @BeforeEach
    void setUp() {
        predicateFactory = new BusinessRulePredicateFactory();
        predicateFactory.setRuleEngine(ruleEngine);
    }

    @Nested
    @DisplayName("Traffic Splitting Tests")
    class TrafficSplittingTests {

        @Test
        @DisplayName("Should route traffic based on percentage rule")
        void shouldRouteTrafficBasedOnPercentageRule() {
            // Arrange
            BusinessRulePredicateFactory.Config config = new BusinessRulePredicateFactory.Config();
            Map<String, Object> ruleParams = new HashMap<>();
            ruleParams.put("percentage", 30);
            config.setRuleType("PERCENTAGE_SPLIT");
            config.setRuleParams(ruleParams);
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                    .header("X-Request-ID", "test-123")
                    .build()
            );
            
            when(ruleEngine.evaluatePercentageSplit(any(), any())).thenReturn(true);
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isTrue();
        }
    }

    @Nested
    @DisplayName("A/B Testing Tests")
    class ABTestingTests {

        @Test
        @DisplayName("Should route traffic based on user group")
        void shouldRouteTrafficBasedOnUserGroup() {
            // Arrange
            BusinessRulePredicateFactory.Config config = new BusinessRulePredicateFactory.Config();
            Map<String, Object> ruleParams = new HashMap<>();
            ruleParams.put("userGroups", "beta-testers,early-adopters");
            config.setRuleType("USER_GROUP");
            config.setRuleParams(ruleParams);
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                    .header("X-User-Group", "beta-testers")
                    .build()
            );
            
            when(ruleEngine.evaluateUserGroup(any(), any())).thenReturn(true);
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isTrue();
        }

        @Test
        @DisplayName("Should route traffic based on feature flag")
        void shouldRouteTrafficBasedOnFeatureFlag() {
            // Arrange
            BusinessRulePredicateFactory.Config config = new BusinessRulePredicateFactory.Config();
            Map<String, Object> ruleParams = new HashMap<>();
            ruleParams.put("featureFlag", "new-ui");
            config.setRuleType("FEATURE_FLAG");
            config.setRuleParams(ruleParams);
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                    .header("X-Features", "new-ui,dark-mode")
                    .build()
            );
            
            when(ruleEngine.evaluateFeatureFlag(any(), any())).thenReturn(true);
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isTrue();
        }
    }

    @Nested
    @DisplayName("Custom Business Logic Tests")
    class CustomBusinessLogicTests {

        @Test
        @DisplayName("Should route traffic based on custom business rule")
        void shouldRouteTrafficBasedOnCustomBusinessRule() {
            // Arrange
            BusinessRulePredicateFactory.Config config = new BusinessRulePredicateFactory.Config();
            Map<String, Object> ruleParams = new HashMap<>();
            ruleParams.put("ruleScript", "request.getHeader('X-Business-Value') > 1000");
            config.setRuleType("CUSTOM_RULE");
            config.setRuleParams(ruleParams);
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                    .header("X-Business-Value", "2000")
                    .build()
            );
            
            when(ruleEngine.evaluateCustomRule(any(), any())).thenReturn(true);
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isTrue();
        }
    }

    @Nested
    @DisplayName("Rule Combination Tests")
    class RuleCombinationTests {

        @Test
        @DisplayName("Should combine multiple rules with AND logic")
        void shouldCombineMultipleRulesWithAndLogic() {
            // Arrange
            BusinessRulePredicateFactory.Config config = new BusinessRulePredicateFactory.Config();
            Map<String, Object> ruleParams = new HashMap<>();
            ruleParams.put("userGroups", "beta-testers");
            ruleParams.put("percentage", 50);
            config.setRuleType("COMBINED");
            config.setRuleParams(ruleParams);
            config.setCombinationOperator("AND");
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                    .header("X-User-Group", "beta-testers")
                    .header("X-Request-ID", "test-123")
                    .build()
            );
            
            when(ruleEngine.evaluateCombinedRules(any(), any(), any())).thenReturn(true);
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isTrue();
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle invalid rule type gracefully")
        void shouldHandleInvalidRuleTypeGracefully() {
            // Arrange
            BusinessRulePredicateFactory.Config config = new BusinessRulePredicateFactory.Config();
            config.setRuleType("INVALID_RULE_TYPE");
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").build()
            );
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isFalse();
        }

        @Test
        @DisplayName("Should handle missing rule parameters gracefully")
        void shouldHandleMissingRuleParametersGracefully() {
            // Arrange
            BusinessRulePredicateFactory.Config config = new BusinessRulePredicateFactory.Config();
            config.setRuleType("PERCENTAGE_SPLIT");
            // Intentionally not setting ruleParams
            
            ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").build()
            );
            
            // Act
            Predicate<ServerWebExchange> predicate = predicateFactory.apply(config);
            
            // Assert
            assertThat(predicate.test(exchange)).isFalse();
        }
    }
} 