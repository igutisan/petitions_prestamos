# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

This is a **Spring WebFlux** reactive petitions management system built using **Hexagonal Architecture** (Ports and Adapters) and **Clean Architecture** principles. The codebase is structured to isolate business logic from infrastructure concerns and uses reactive programming with Project Reactor.

## Architecture

The project follows these architectural layers:
- **Domain**: Core business logic and entities (independent of frameworks)
  - `domain/model`: Business entities and port interfaces (gateways)
  - `domain/usecase`: Business logic implementation
- **Infrastructure**: Technology-specific implementations
  - `infrastructure/driven-adapters`: Outbound adapters (database, external APIs)
  - `infrastructure/entry-points`: Inbound adapters (REST API)
- **Applications**: Application assembly and configuration
  - `applications/app-service`: Main Spring Boot application and DI configuration

## Common Commands

### Build and Run

```bash
# Build the project
./gradlew clean build

# Run the application
./gradlew bootRun

# Run with Docker Compose (includes PostgreSQL)
docker-compose up -d

# Build Docker image
./gradlew build
docker build -f deployment/Dockerfile -t petitions-app applications/app-service/build/libs/
```

### Testing

```bash
# Run all tests
./gradlew test

# Run tests with coverage
./gradlew test jacocoTestReport

# Run mutation tests
./gradlew pitest

# Run a specific test class
./gradlew test --tests "co.com.pragma.api.RouterRestTest"

# Generate aggregated coverage report
./gradlew jacocoMergedReport
```

### Code Quality

```bash
# Run SonarQube analysis
./gradlew sonarqube

# Check project structure
./gradlew validateStructure
```

### Database

```bash
# Start PostgreSQL container only
docker-compose up -d postgres

# Connect to database
docker exec -it postgres-petitions psql -U test -d petitions
```

Database runs on port 5433 (not standard 5432).

## Key Technical Details

### Reactive Programming
- Uses **Spring WebFlux** with Project Reactor
- All operations return `Mono<T>` or `Flux<T>`
- **NEVER use `.block()`** - it defeats the reactive paradigm
- Use `StepVerifier` for testing reactive streams

### Dependency Injection
- Uses constructor injection via Spring
- All use cases are configured in `UseCasesConfig.java`
- Interfaces (ports) are defined in domain, implementations (adapters) in infrastructure

### Security
- JWT-based authentication implemented
- Security configuration in `infrastructure/entry-points/reactive-web/src/main/java/co/com/pragma/api/config/SecurityConfig.java`

### Database Access
- Uses R2DBC for reactive PostgreSQL access
- Connection configuration in `infrastructure/driven-adapters/r2dbc-postgresql`

## Development Guidelines

### SOLID Principles in Reactive Context
- **Single Responsibility**: Each reactive pipeline should handle one business capability
- **Open/Closed**: Depend on interfaces (ports), not concrete implementations
- **Liskov Substitution**: All reactive types must complete or error predictably
- **Interface Segregation**: Create specific interfaces for client needs
- **Dependency Inversion**: Controllers depend on UseCase interfaces, UseCases depend on Gateway interfaces

### Error Handling
- Use custom exceptions in domain layer
- Implement global exception handler with `@RestControllerAdvice`
- Use reactive error operators: `onErrorResume`, `onErrorMap`, `doOnError`

### Testing Approach
- Unit tests for use cases with mocked dependencies
- Use `StepVerifier` for reactive stream assertions
- Integration tests with `WebTestClient` for REST endpoints

## Project-Specific Rules

### From GEMINI.md (Reactive Best Practices):
- Maintain immutability - use Java records where possible
- Use `flatMap` for async transformations (T -> Mono<U>)
- Use `map` for sync transformations (T -> U)
- Apply `subscribeOn` and `publishOn` carefully to control threading
- Log reactively with `doOnNext`, `doOnError`, or `log()`

### From guidelines.md (Flow Example):
1. HTTP request → RouterRest → Handler
2. Handler → UseCase (business logic)
3. UseCase → Gateway interfaces (ports)
4. Gateway implementations → External systems
5. Response flows back through the chain

## Technology Stack
- **Language**: Java 21
- **Framework**: Spring Boot 3.5.4 / Spring WebFlux
- **Build**: Gradle 8.14.3 (multi-module)
- **Database**: PostgreSQL with R2DBC
- **Testing**: JUnit 5, Reactor Test, Pitest
- **Code Generation**: Lombok, MapStruct
- **Containerization**: Docker

## Module Dependencies
- Domain modules have no external dependencies
- Infrastructure modules depend on domain interfaces
- Application module assembles and configures all components
