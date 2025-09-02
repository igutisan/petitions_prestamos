# Project Architecture Guidelines

## 1. Overview

This project is structured following the principles of **Hexagonal Architecture** (also known as Ports and Adapters). The core idea is to isolate the central business logic and domain of the application from external concerns like databases, user interfaces, or third-party services.

This separation is achieved by defining "ports" (interfaces) in the core domain, which are then implemented by "adapters" in the infrastructure layer.

The project is organized into three main layers:
- `domain`: The core of the application.
- `infrastructure`: Contains implementations of external-facing technology.
- `applications`: The runnable application, responsible for wiring everything together.

---

## 2. Module Breakdown

### `domain`
This is the heart of the application and is completely independent of any external technology.

- **`domain/model`**:
    - Contains the business entities (e.g., `Petition`, `LoanStatus`).
    - Defines the "ports" as Java interfaces (gateways) that the business logic needs to interact with the outside world (e.g., `PetitionRepository`, `AuthenticationGateway`, `LoggerGateway`).
    - Contains domain-specific exceptions (e.g., `InvalidUser`).

- **`domain/usecase`**:
    - Implements the core business logic and application services.
    - Orchestrates the domain models and uses the ports (interfaces) defined in `domain/model` to perform its tasks. For example, `PetitionUseCase` contains the logic for managing petitions.

### `infrastructure`
This layer provides the concrete implementations (adapters) for the ports defined in the domain. It's divided into two main categories:

- **`infrastructure/entry-points` (Driving Adapters)**:
    - These adapters drive the application. They are the primary way the outside world interacts with the core logic.
    - **`reactive-web`**: Implements a reactive REST API using Spring WebFlux. It receives HTTP requests, maps them to DTOs, calls the appropriate `UseCase`, and returns a response.

- **`infrastructure/driven-adapters` (Secondary Adapters)**:
    - These adapters are driven by the application core. They are the implementations of the repository and service interfaces (ports).
    - **`r2dbc-postgresql`**: Implements the `PetitionRepository` port for data persistence using a reactive connection to a PostgreSQL database.
    - **`rest-consumer`**: Implements the `AuthenticationGateway` port by consuming an external REST service to validate users.
    - **`logger-adapter`**: Implements the `LoggerGateway` port to provide a structured logging mechanism.

### `applications`
This layer is responsible for the configuration and assembly of the entire application.

- **`app-service`**:
    - Contains the main Spring Boot application class (`MainApplication.java`).
    - Handles the Dependency Injection (DI) wiring. `UseCasesConfig.java` is where the use cases are instantiated and the specific adapter implementations (from the `infrastructure` layer) are injected into them.

---

## 3. Flow of Control (Example: Creating a Petition)

1.  An **HTTP POST request** hits the endpoint defined in `infrastructure/entry-points/reactive-web/RouterRest`.
2.  The `Handler` class receives the request and its `CreatePetitionDTO` payload.
3.  The `Handler` calls a method in the `domain/usecase/PetitionUseCase`.
4.  The `PetitionUseCase` executes the core business logic. It might first use the `AuthenticationGateway` port to validate the user.
    - The call to the `AuthenticationGateway` interface is resolved by the DI container to its implementation in `infrastructure/driven-adapters/rest-consumer`.
5.  Next, the `UseCase` uses the `PetitionRepository` port to save the new `Petition` entity.
    - This call is resolved to the implementation in `infrastructure/driven-adapters/r2dbc-postgresql`, which saves the data to the database.
6.  The result is returned up the call stack and the `Handler` maps the domain entity to a `PetitionResponseDTO` and sends it as the HTTP response.

---

## 4. Technology Stack

- **Language**: Java
- **Framework**: Spring Boot / Spring WebFlux (Reactive)
- **Build Tool**: Gradle (Multi-module project)
- **Database**: PostgreSQL (with R2DBC for reactive access)
- **Auxiliary**: Lombok
- **Deployment**: Docker
