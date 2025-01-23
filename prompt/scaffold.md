# Cursor Prompt for Enterprise-Level SaaS Project Setup

## Project Overview
Create a high-performance, scalable SaaS platform designed to support:
- 200 million global users
- 20 million concurrent users
- High usage during work hours
- 30% of workday usage during non-work hours
- Multi-terminal support

## Project Structure Requirements

### 父项目 (dove-parent)
- Create a standalone Maven project for managing shared dependencies
- Location: Independent root directory
- Purpose: Manage shared dependencies, plugins, and version configurations
- Centralized version management for all child project dependencies
- Plugin Management Configuration:
  - Configure pluginManagement section with:
    - maven-compiler-plugin
    - maven-surefire-plugin
    - maven-jar-plugin
    - spring-boot-maven-plugin
    - Additional necessary plugins based on requirements
- Multi-environment Profile Configuration:
  - Development environment
  - Testing environment
  - Production environment
- Static Code Analysis Configuration:
  - maven-checkstyle-plugin
  - maven-pmd-plugin
  - Configuration examples for code quality checks

### Child Project (dove-api-gateway)
- Create a Spring Cloud API Gateway microservice
- Must inherit from dove-parent
- Based on OpenJDK 17 (latest stable version)
- Location: Independent subdirectory
- Must be fully runnable out of the box with a single command

### 目录结构
```
dove/
├── dove-parent/
│   └── pom.xml
└── dove-api-gateway/
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   │   └── com/helix/dove/
    │   │   │       ├── config/
    │   │   │       │   ├── GatewayConfig.java           # Gateway routing configuration
    │   │   │       │   ├── CacheConfig.java             # Cache configuration (Caffeine + Redis)
    │   │   │       │   ├── MetricsConfig.java           # Metrics collection configuration
    │   │   │       │   ├── SecurityConfig.java          # Security and SSL configuration
    │   │   │       │   └── predicates/                  # Custom predicate factories
    │   │   │       │       ├── TimeBasedRoutePredicateFactory.java
    │   │   │       │       ├── GeoLocationPredicateFactory.java
    │   │   │       │       ├── BusinessRulePredicateFactory.java
    │   │   │       │       └── LoadBalancePredicateFactory.java
    │   │   │       ├── controller/
    │   │   │       │   ├── HealthController.java        # Health check endpoints
    │   │   │       │   ├── RouteController.java         # Route management API
    │   │   │       │   └── MetricsController.java       # Metrics endpoints
    │   │   │       ├── dto/
    │   │   │       │   ├── ResponseDTO.java             # Common response wrapper
    │   │   │       │   ├── RouteDTO.java                # Route data transfer object
    │   │   │       │   └── MetricsDTO.java              # Metrics data transfer object
    │   │   │       ├── entity/
    │   │   │       │   ├── RouteEntity.java             # Route configuration entity
    │   │   │       │   ├── PredicateEntity.java         # Predicate configuration entity
    │   │   │       │   ├── FilterEntity.java            # Filter configuration entity
    │   │   │       │   └── AuditEntity.java             # Audit log entity
    │   │   │       ├── repository/
    │   │   │       │   ├── RouteRepository.java         # Route data access
    │   │   │       │   ├── PredicateRepository.java     # Predicate data access
    │   │   │       │   ├── FilterRepository.java        # Filter data access
    │   │   │       │   └── AuditRepository.java         # Audit log data access
    │   │   │       ├── service/
    │   │   │       │   ├── RouteService.java            # Route business logic
    │   │   │       │   ├── CacheService.java            # Cache management
    │   │   │       │   ├── MetricsService.java          # Metrics collection
    │   │   │       │   └── AuditService.java            # Audit logging
    │   │   │       ├── utils/
    │   │   │       │   ├── RouteUtil.java               # Route utility functions
    │   │   │       │   ├── PredicateUtil.java           # Predicate utility functions
    │   │   │       │   ├── FilterUtil.java              # Filter utility functions
    │   │   │       │   └── SecurityUtil.java            # Security utility functions
    │   │   │       ├── event/
    │   │   │       │   ├── RouteChangeEvent.java        # Route change events
    │   │   │       │   └── RouteEventListener.java      # Event listeners
    │   │   │       ├── exception/
    │   │   │       │   ├── RouteException.java          # Custom exceptions
    │   │   │       │   └── ExceptionHandler.java        # Global exception handler
    │   │   │       └── ProjectApplication.java          # Main application class
    │   │   └── resources/
    │   │       ├── config/                              # Configuration files
    │   │       │   ├── route-rules.yml                  # Route rules configuration
    │   │       │   ├── cache-config.yml                 # Cache configuration
    │   │       │   ├── circuit-breaker.yml              # Circuit breaker configuration
    │   │       │   ├── rate-limit.yml                   # Rate limiting rules
    │   │       │   └── monitoring.yml                   # Monitoring configuration
    │   │       ├── application.yml                      # Application configuration
    │   │       ├── application-dev.yml                  # Development profile
    │   │       ├── application-test.yml                 # Testing profile
    │   │       ├── application-prod.yml                 # Production profile
    │   │       ├── schema.sql                           # Database schema
    │   │       └── data.sql                             # Initial data
    │   └── test/
    │       └── java/
    │           └── com/helix/dove/
    │               ├── config/
    │               │   ├── GatewayConfigTest.java       # Configuration tests
    │               │   └── predicates/                  # Predicate factory tests
    │               │       ├── TimeBasedPredicateTest.java
    │               │       ├── GeoLocationPredicateTest.java
    │               │       └── BusinessRulePredicateTest.java
    │               ├── controller/
    │               │   ├── RouteControllerTest.java     # Controller tests
    │               │   └── MetricsControllerTest.java   # Metrics endpoint tests
    │               ├── service/
    │               │   ├── RouteServiceTest.java        # Service tests
    │               │   └── CacheServiceTest.java        # Cache tests
    │               ├── integration/
    │               │   ├── RouteIntegrationTest.java    # Integration tests
    │               │   └── CacheIntegrationTest.java    # Cache integration tests
    │               └── performance/
    │                   ├── RoutingPerformanceTest.java  # Performance tests
    │                   └── LoadTest.java                # Load tests
    ├── k8s/                                            # Kubernetes manifests
    │   ├── deployment.yml
    │   ├── service.yml
    │   ├── configmap.yml
    │   └── secret.yml
    ├── scripts/                                        # Utility scripts
    │   ├── startup.sh
    │   ├── health-check.sh
    │   └── cleanup.sh
    ├── pom.xml
    ├── Dockerfile
    └── docker-compose.yml

```

### Required File Contents
Each file should contain:

1. GatewayConfig.java:
   - Spring Cloud Gateway route configurations:
     * Basic route configuration using RouteLocatorBuilder
     * Multiple routing dimensions support:
       - Path-based routing with Ant-style pattern matching
       - Host-based routing with domain pattern matching
       - Header-based routing for custom header values
       - Query parameter-based routing
       - HTTP method-based routing
     * Regular expression support in route patterns
     * Weight-based routing implementation
     * Custom Predicate Factory implementation:
       - TimeBasedRoutePredicateFactory for time-window routing
       - GeoLocationPredicateFactory for location-based routing
       - Custom business logic predicates
     * Route configuration persistence with Nacos
     * Dynamic route updates support
     * Route caching implementation:
       - Local cache using Caffeine (with specific cache size and TTL settings)
       - Distributed cache using Redis (with cluster mode configuration)
     * Monitoring and metrics collection points
     * Circuit breaker integration
     * Rate limiting configuration
     * Retry policies
     * Error handling and fallback routes
     * Global configuration:
       - Default backpressure configuration
       - Default timeout settings
       - Default retry settings
       - Default circuit breaker settings
     * Performance optimization:
       - Thread pool configuration for optimal performance
       - Connection pool settings
       - Buffer size optimization
     * High availability support:
       - Cluster mode configuration
       - Load balancing strategy
       - Failover configuration
     * Global routing strategy:
       - Region-aware routing
       - Multi-datacenter support
       - Cross-region failover
     * Monitoring integration:
       - Prometheus metrics export
       - Grafana dashboard templates
       - Custom health indicators

2. HealthController.java:
   - Health check endpoint
   - Basic monitoring endpoints

3. ResponseDTO.java:
   - Generic response wrapper class
   - Standard fields: code, message, data

4. RouteEntity.java:
   - Route configuration entity with the following fields:
     * id: Long (Primary Key)
     * routeId: String (Unique identifier)
     * serviceName: String (Target service name)
     * path: String (Route path pattern)
     * host: String (Host pattern)
     * headers: Map<String, String> (Required headers)
     * queryParams: Map<String, String> (Required query parameters)
     * methods: Set<String> (Allowed HTTP methods)
     * uri: String (Target URI)
     * predicates: List<PredicateDefinition> (Route predicates)
     * filters: List<FilterDefinition> (Route filters)
     * order: Integer (Route order)
     * metadata: Map<String, Object> (Additional metadata)
     * enabled: Boolean (Route status)
     * weight: Integer (Route weight if applicable)
     * createdTime: LocalDateTime
     * updatedTime: LocalDateTime

5. RouteRepository.java:
   - Route data access interface with:
     * Standard CRUD operations
     * Custom queries:
       - findByServiceName
       - findByEnabled
       - findByPathPattern
       - findByHostPattern
     * Batch operations support
     * Cache annotations
     * Optimistic locking support
     * Audit logging

6. RouteService.java:
   - Route management business logic:
     * Dynamic route registration
     * Route update and deletion
     * Route cache management
     * Route validation logic
     * Weight calculation and balancing
     * Route metrics collection
     * Route health checking
     * Predicate evaluation
     * Filter chain management
     * Error handling and recovery
     * Event publishing for route changes
     * Integration with service discovery
     * Route version management
     * Route rollback support

7. RouteUtil.java:
   - Utility methods for route handling:
     * Route ID generation
     * URI validation and normalization
     * Predicate parsing and validation
     * Filter parsing and validation
     * Pattern matching utilities
     * Weight calculation helpers
     * Cache key generation
     * Metric name generation
     * Error message formatting
     * Route comparison utilities
     * Configuration validation
     * Security check utilities

8. application.yml:
   - Server configuration:
     * Server port and context path
     * Spring Cloud Gateway configuration:
       - Default filters
       - Global CORS settings
       - Timeout settings
       - Retry settings
       - Circuit breaker settings
     * Route definitions:
       - Path routes
       - Host routes
       - Method routes
       - Weight routes
       - Custom predicate routes
     * Cache configuration:
       - Caffeine settings
       - Redis settings
     * Service discovery:
       - Nacos configuration
       - Load balancer settings
     * Monitoring:
       - Metrics endpoints
       - Health check settings
       - Logging configuration
     * Security:
       - SSL settings
       - CORS settings
       - Rate limiting
     * Multi-environment profiles:
       - Development
       - Testing
       - Production
     * Performance tuning:
       - Thread pool settings
       - Connection pool settings
       - Buffer size settings
     * Circuit breaker configuration:
       - Timeout settings
       - Fallback settings
       - Retry settings

9. schema.sql:
   - Route configuration tables:
     * route_config table
     * route_predicate table
     * route_filter table
     * route_metadata table
   - Required indexes:
     * route_id_idx
     * service_name_idx
     * path_pattern_idx
   - Audit tables:
     * route_audit_log
     * route_version_history

10. data.sql:
    - Initial route configurations:
      * Default routes
      * Common service routes
      * Example routes for each type:
        - Path-based
        - Host-based
        - Method-based
        - Weight-based
        - Custom predicate
    - Test data for development
    - Required metadata
    - Default settings

### Version Requirements
- All dependencies must be:
  - Open source
  - Latest stable versions
  - Currently supported
  - Compatible with OpenJDK 17

## Requirements
- Must generate all specified files and directories
- Each file must contain the specified functionality
- Project must be fully runnable out of the box with a single command
- All configuration must support multi-environment deployment

### Test Requirements

1. GatewayConfigTest.java:
   - Test configuration loading:
     * Verify all route configurations are loaded correctly
     * Test different environment profiles
     * Validate custom predicate factories registration
   - Test route definitions:
     * Test path-based routing
     * Test host-based routing
     * Test header-based routing
     * Test query parameter routing
     * Test method-based routing
     * Test weight-based routing
     * Test regex pattern matching
   - Test caching:
     * Verify Caffeine cache configuration
     * Verify Redis cache configuration
   - Test circuit breaker:
     * Verify circuit breaker settings
     * Test fallback routes
   - Test rate limiting:
     * Verify rate limit configurations
     * Test rate limit enforcement

2. PredicateFactoryTests:
   - TimeBasedRoutePredicateFactoryTest.java:
     * Test time window validation
     * Test different time zones
     * Test edge cases (midnight, DST changes)
   - GeoLocationPredicateFactoryTest.java:
     * Test location matching
     * Test coordinate calculations
     * Test region restrictions
   - BusinessRulePredicateFactoryTest.java:
     * Test custom business rules
     * Test rule combinations
     * Test rule priorities

3. RouteControllerTest.java:
   - Test API endpoints:
     * Test route creation
     * Test route updates
     * Test route deletion
     * Test route queries
   - Test validation:
     * Test invalid inputs
     * Test missing required fields
     * Test duplicate routes
   - Test error handling:
     * Test error responses
     * Test validation errors
     * Test not found scenarios
   - Test security:
     * Test authentication
     * Test authorization
     * Test CORS settings

4. RouteServiceTest.java:
   - Test route management:
     * Test route registration
     * Test route updates
     * Test route deletion
     * Test route activation/deactivation
   - Test cache management:
     * Test cache updates
     * Test cache invalidation
     * Test cache synchronization
   - Test route validation:
     * Test URI validation
     * Test predicate validation
     * Test filter validation
   - Test weight balancing:
     * Test weight calculations
     * Test load distribution
   - Test metrics:
     * Test metrics collection
     * Test performance monitoring
   - Test events:
     * Test event publishing
     * Test event handling
   - Test error scenarios:
     * Test conflict handling
     * Test timeout handling
     * Test circuit breaker triggering

5. CacheServiceTest.java:
   - Test cache operations:
     * Test cache put operations
     * Test cache get operations
     * Test cache eviction
   - Test cache synchronization:
     * Test multi-node synchronization
     * Test cache consistency
   - Test cache failures:
     * Test cache timeout
     * Test cache recovery

6. MetricsServiceTest.java:
   - Test metrics collection:
     * Test performance metrics
     * Test business metrics
     * Test custom metrics
   - Test aggregation:
     * Test metric aggregation
     * Test time-based aggregation
   - Test alerting:
     * Test threshold alerts
     * Test trend alerts

7. SecurityUtilTest.java:
   - Test security functions:
     * Test authentication validation
     * Test authorization checks
     * Test token validation
   - Test encryption:
     * Test data encryption
     * Test data decryption
   - Test security patterns:
     * Test security patterns validation
     * Test security rule enforcement

8. Integration Tests:
   - RouteIntegrationTest.java:
     * Test complete route lifecycle
     * Test route updates with cache
     * Test route metrics collection
   - CacheIntegrationTest.java:
     * Test cache with real Redis
     * Test multi-node scenarios
   - SecurityIntegrationTest.java:
     * Test complete security flow
     * Test with real authentication

9. Performance Tests:
   - RoutingPerformanceTest.java:
     * Test routing latency
     * Test concurrent requests
     * Test memory usage
   - CachePerformanceTest.java:
     * Test cache hit rates
     * Test cache response times
   - LoadTest.java:
     * Test system under load
     * Test recovery after load

### Test Coverage Requirements:
- Minimum 85% line coverage
- Minimum 90% branch coverage
- 100% coverage for critical components:
  * Route configuration
  * Security checks
  * Cache management
  * Error handling

### Test Best Practices:
- Use appropriate test categories:
  * @UnitTest
  * @IntegrationTest
  * @PerformanceTest
- Follow AAA pattern:
  * Arrange
  * Act
  * Assert
- Use meaningful test names:
  * shouldDoSomethingWhenSomething
  * givenWhenThen format
- Include both positive and negative tests
- Test edge cases and boundary conditions
- Use appropriate test data:
  * Test data builders
  * Meaningful test data
  * Boundary values
- Clean up test resources:
  * Use @Before and @After
  * Clean up test data
  * Reset test state

### Test Dependencies:
- JUnit 5
- Mockito
- AssertJ
- TestContainers for integration tests
- WireMock for external service mocking
- JMeter for performance tests

### Custom Predicate Factories:
- Create the following files under config/predicates/:
  * TimeBasedRoutePredicateFactory.java:
    - Support for multiple time zones
    - Business hour awareness
    - Holiday calendar integration
  * GeoLocationPredicateFactory.java:
    - IP-based geolocation
    - Region-based routing
    - Custom geography rules
  * BusinessRulePredicateFactory.java:
    - Traffic splitting rules
    - A/B testing support
    - Custom business logic
  * LoadBalancePredicateFactory.java:
    - Load-aware routing
    - Resource utilization based routing
    - Service health awareness

### Additional Configuration Files:
- Under resources/config:
  * route-rules.yml:
    - Default routing rules
    - Environment-specific rules
    - Region-specific rules
  * cache-config.yml:
    - Caffeine cache settings
    - Redis cluster configuration
    - Cache synchronization settings
  * circuit-breaker.yml:
    - Circuit breaker configurations
    - Fallback definitions
    - Retry policies
  * rate-limit.yml:
    - Rate limit rules
    - Throttling configurations
    - Quota definitions
  * monitoring.yml:
    - Metrics configuration
    - Alert rules
    - Dashboard definitions

### Docker Support:
- Dockerfile:
  * Multi-stage build
  * JVM optimization
  * Health check configuration
- docker-compose.yml:
  * Service definitions
  * Dependencies configuration
  * Network settings
  * Volume mappings

### Kubernetes Support:
- k8s/
  * deployment.yml:
    - Pod specifications
    - Resource limits
    - Health checks
    - Auto-scaling rules
  * service.yml:
    - Service exposure
    - Load balancing
  * configmap.yml:
    - Configuration management
  * secret.yml:
    - Sensitive data management

### Startup Scripts:
- scripts/
  * startup.sh:
    - Environment setup
    - Configuration validation
    - Service startup
  * health-check.sh:
    - Health verification
    - Dependency checks
  * cleanup.sh:
    - Resource cleanup
    - Cache clearing