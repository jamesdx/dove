# Cursor Prompt for Dove Authentication Service (认证服务)

## Project Overview (项目概述)
Create a high-performance, scalable authentication service designed to support:
- 200 million global users (2亿全球用户)
- 80 million concurrent users (8000万并发用户)
- High availability (99.999%) (高可用性)
- Multi-protocol authentication support (多协议认证支持)
- Enterprise-grade security (企业级安全)

## Project Structure Requirements (项目结构要求)

### Child Project (dove-auth)
- Must inherit from dove-parent
- Based on OpenJDK 17 (latest stable version)
- Spring Cloud + Spring Cloud Alibaba based
- Location: Independent subdirectory under dove project
- Must be fully runnable out of the box
## Maven 说明
dove-parent 是maven 项目的 父项目，dove-auth 是maven 项目的 子项目，dove-auth 要继承 dove-parent项目
### Directory Structure (目录结构)
```
dove-auth/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/helix/dove/auth/
│   │   │       ├── config/
│   │   │       │   ├── AuthServerConfig.java          # Authentication server configuration
│   │   │       │   ├── OAuth2Config.java              # OAuth2 configuration
│   │   │       │   ├── SAMLConfig.java                # SAML configuration
│   │   │       │   ├── SecurityConfig.java            # Security configuration
│   │   │       │   ├── RedisConfig.java               # Redis configuration
│   │   │       │   └── SwaggerConfig.java             # API documentation
│   │   │       ├── controller/
│   │   │       │   ├── AuthController.java            # Authentication endpoints
│   │   │       │   ├── OAuth2Controller.java          # OAuth2 endpoints
│   │   │       │   ├── SAMLController.java            # SAML endpoints
│   │   │       │   ├── TokenController.java           # Token management
│   │   │       │   └── MFAController.java             # Multi-factor auth
│   │   │       ├── service/
│   │   │       │   ├── AuthenticationService.java     # Core authentication
│   │   │       │   ├── TokenService.java              # Token management
│   │   │       │   ├── OAuth2Service.java             # OAuth2 operations
│   │   │       │   ├── SAMLService.java               # SAML operations
│   │   │       │   ├── MFAService.java                # MFA operations
│   │   │       │   └── AuditService.java              # Security audit
│   │   │       ├── domain/
│   │   │       │   ├── entity/
│   │   │       │   │   ├── AuthToken.java             # Token entity
│   │   │       │   │   ├── OAuth2Client.java          # OAuth2 client
│   │   │       │   │   ├── SAMLConfig.java            # SAML config
│   │   │       │   │   └── MFAConfig.java             # MFA settings
│   │   │       │   └── dto/
│   │   │       │       ├── LoginRequest.java          # Login request
│   │   │       │       ├── TokenResponse.java         # Token response
│   │   │       │       └── AuthResponse.java          # Auth response
│   │   │       ├── repository/
│   │   │       │   ├── TokenRepository.java           # Token storage
│   │   │       │   ├── OAuth2Repository.java          # OAuth2 data
│   │   │       │   └── SAMLRepository.java            # SAML data
│   │   │       ├── security/
│   │   │       │   ├── filter/
│   │   │       │   │   ├── JwtAuthFilter.java         # JWT filter
│   │   │       │   │   └── MFAAuthFilter.java         # MFA filter
│   │   │       │   ├── handler/
│   │   │       │   │   ├── AuthSuccessHandler.java    # Success handler
│   │   │       │   │   └── AuthFailureHandler.java    # Failure handler
│   │   │       │   └── provider/
│   │   │       │       ├── JwtProvider.java           # JWT provider
│   │   │       │       └── MFAProvider.java           # MFA provider
│   │   │       ├── util/
│   │   │       │   ├── JwtUtil.java                   # JWT utilities
│   │   │       │   ├── EncryptionUtil.java            # Encryption
│   │   │       │   └── ValidationUtil.java            # Validation
│   │   │       └── AuthApplication.java               # Main class
│   │   └── resources/
│   │       ├── application.yml                        # Base config
│   │       ├── application-dev.yml                    # Dev profile
│   │       ├── application-test.yml                   # Test profile
│   │       ├── application-prod.yml                   # Prod profile
│   │       ├── bootstrap.yml                          # Bootstrap config
│   │       └── i18n/                                  # Internationalization
│   │           ├── messages_en.properties
│   │           └── messages_zh.properties
│   └── test/
│       └── java/
│           └── com/helix/dove/auth/
│               ├── controller/
│               │   ├── AuthControllerTest.java
│               │   └── OAuth2ControllerTest.java
│               ├── service/
│               │   ├── AuthenticationServiceTest.java
│               │   └── TokenServiceTest.java
│               └── security/
│                   └── JwtProviderTest.java
├── k8s/                                              # Kubernetes configs
│   ├── deployment.yml
│   ├── service.yml
│   └── configmap.yml
├── docker/
│   └── Dockerfile
├── pom.xml
└── README.md
```

### Required File Contents (必需文件内容)

1. AuthServerConfig.java:
   - Authentication server configurations
   - Security settings
   - Token management
   - Session management
   - Rate limiting
   - Circuit breaker settings

2. OAuth2Config.java:
   - OAuth2 provider settings
   - Client registration
   - Token endpoints
   - Authorization endpoints
   - Scope configurations

3. SAMLConfig.java:
   - SAML 2.0 configurations
   - Identity provider settings
   - Service provider settings
   - Metadata configurations
   - Certificate management

4. SecurityConfig.java:
   - Web security configuration
   - Authentication providers
   - Password encoders
   - CORS settings
   - CSRF protection
   - Security filters

5. AuthController.java:
   - Login endpoints
   - Logout endpoints
   - Password reset
   - MFA verification
   - Session management

6. TokenService.java:
   - Token generation
   - Token validation
   - Token refresh
   - Token revocation
   - Token storage

7. application.yml:
```yaml
server:
  port: 8081
  servlet:
    context-path: /auth

spring:
  application:
    name: dove-auth
  cloud:
    nacos:
      discovery:
        server-addr: ${NACOS_SERVER:localhost:8848}
  datasource:
    url: jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/dove_auth
    username: ${DB_USER:root}
    password: ${DB_PASSWORD:root}
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PASSWORD:}

security:
  jwt:
    secret: ${JWT_SECRET:your-secret-key}
    expiration: 86400000
  oauth2:
    client:
      registration:
        google:
          client-id: ${GOOGLE_CLIENT_ID}
          client-secret: ${GOOGLE_CLIENT_SECRET}
  saml:
    keystore:
      location: saml/keystore.jks
      password: ${SAML_KEYSTORE_PASSWORD}
      alias: ${SAML_CERT_ALIAS}

logging:
  level:
    com.helix.dove: ${LOG_LEVEL:INFO}
```

### Test Requirements (测试要求)

1. Unit Tests:
   - Controller layer tests
   - Service layer tests
   - Repository layer tests
   - Security component tests
   - Utility class tests

2. Integration Tests:
   - Authentication flow tests
   - OAuth2 flow tests
   - SAML flow tests
   - MFA flow tests
   - Token management tests

3. Performance Tests:
   - Concurrent user load tests
   - Token generation performance
   - Authentication response time
   - Cache hit ratio tests
   - Database performance tests

4. Security Tests:
   - Penetration testing
   - Security scanning
   - Token security tests
   - Password security tests
   - Session security tests

### Test Coverage Requirements (测试覆盖率要求):
- Minimum 85% line coverage
- Minimum 90% branch coverage
- 100% coverage for security-critical components

### Dependencies (依赖项)
- Spring Cloud
- Spring Security
- Spring OAuth2
- Spring SAML
- JWT
- Redis
- MySQL
- Caffeine
- Swagger
- JUnit 5
- Mockito
- TestContainers

### Deployment Requirements (部署要求)
- Kubernetes deployment support
- Docker containerization
- Multi-region deployment capability
- Auto-scaling configuration
- Health check endpoints
- Monitoring and metrics
- Log aggregation

### Security Requirements (安全要求)
- HTTPS/TLS encryption
- Password hashing (BCrypt)
- JWT token security
- OAuth2/OIDC compliance
- SAML 2.0 compliance
- MFA support
- Rate limiting
- Audit logging
- Session management
- CORS/CSRF protection 