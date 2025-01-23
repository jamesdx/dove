# Cursor Prompt for Enterprise-Level SaaS Project Setup

## Project Overview
Create a high-performance, scalable SaaS platform designed to support:
- 200 million global users
- 20 million concurrent users
- High usage during work hours
- 30% of workday usage during non-work hours
- Multi-terminal support

## Project Structure Requirements

Create a Maven-based parent-child project structure with the following specifications:

### Parent Project (dove-parent)
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

### Required Files and Directories Structure
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
    │   │   │       │   └── GatewayConfig.java         # Gateway routing configuration
    │   │   │       ├── controller/
    │   │   │       │   └── HealthController.java      # Health check endpoint
    │   │   │       ├── dto/
    │   │   │       │   └── ResponseDTO.java           # Common response wrapper
    │   │   │       ├── entity/
    │   │   │       │   └── RouteEntity.java           # Route configuration entity
    │   │   │       ├── repository/
    │   │   │       │   └── RouteRepository.java       # Route data access
    │   │   │       ├── service/
    │   │   │       │   └── RouteService.java          # Route business logic
    │   │   │       ├── utils/
    │   │   │       │   └── RouteUtil.java             # Utility functions
    │   │   │       └── ProjectApplication.java        # Main application class
    │   │   └── resources/
    │   │       ├── application.yml                    # Application configuration
    │   │       ├── schema.sql                         # Database schema
    │   │       └── data.sql                           # Initial data
    │   └── test/
    │       └── java/
    │           └── com/helix/dove/
    │               └── ProjectApplicationTests.java    # Main test class
    ├── pom.xml
    └── Dockerfile

```

### Required File Contents
Each file should contain:

1. GatewayConfig.java:
   - Spring Cloud Gateway route configurations
   - Custom route predicates and filters

2. HealthController.java:
   - Health check endpoint
   - Basic monitoring endpoints

3. ResponseDTO.java:
   - Generic response wrapper class
   - Standard fields: code, message, data

4. RouteEntity.java:
   - Route configuration entity
   - Fields: id, routeId, serviceName, path, uri

5. RouteRepository.java:
   - Route data access interface
   - CRUD operations for routes

6. RouteService.java:
   - Route management business logic
   - Route refresh and update methods

7. RouteUtil.java:
   - Utility methods for route handling
   - Route ID generation logic

8. application.yml:
   - Server configuration
   - Gateway configuration
   - Multi-environment profiles
   - Logging configuration

9. schema.sql:
   - Route configuration table creation
   - Required indexes

10. data.sql:
    - Initial route configurations
    - Default gateway routes

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