# Dove Parent Project

This is the parent POM project for the Dove microservices architecture. It provides centralized dependency management and build configuration for all Dove modules.

## Features

- Java 17 based development
- Spring Boot 3.x and Spring Cloud 2023.x
- Spring Cloud Alibaba integration
- Centralized dependency management
- Standardized build configuration
- Code quality tools integration
- Test coverage requirements

## Project Structure

```
dove-parent/
├── pom.xml                 # Parent POM file
├── .mvn/                   # Maven wrapper directory
│   └── wrapper/           # Maven wrapper configuration
├── README.md              # Project documentation
└── LICENSE                # License file
```

## Requirements

- JDK 17+
- Maven 3.8+ (or use the included Maven wrapper)

## Usage

To use this parent POM in your project:

```xml
<parent>
    <groupId>com.dove</groupId>
    <artifactId>dove-parent</artifactId>
    <version>1.0.0</version>
    <relativePath/>
</parent>
```

## Building

Using Maven wrapper:

```bash
./mvnw clean install
```

Using Maven directly:

```bash
mvn clean install
```

## Available Profiles

- `dev` (default): Development environment
- `test`: Test environment
- `prod`: Production environment

## License

[Apache License 2.0](LICENSE) 