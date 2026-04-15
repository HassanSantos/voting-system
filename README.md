# Voting System

Spring Boot API for creating voting sessions linked to agendas.

## Tech Stack
- Java 25
- Spring Boot 3.5.13
- Maven Wrapper (`./mvnw`)
- Spring Web
- Springdoc OpenAPI (`springdoc-openapi-starter-webmvc-ui`)

## Project Status
Current codebase already exposes the voting session endpoint contract, but the application is still incomplete:
- `CreateVotingSessionUseCase` exists only as an interface (no production implementation/bean yet).
- `CreateAgendaUseCase` and `Agenda` domain model are present, but agenda endpoints/use case implementations are not wired.

Because of this, the app needs a concrete Spring bean implementation for `CreateVotingSessionUseCase` to run in non-test mode.

## Requirements
- JDK 25

## Run
```bash
./mvnw spring-boot:run
```

## Test
```bash
./mvnw test
```

Known limitation:
- In this environment (JDK 25.0.2), tests fail with a Mockito/ByteBuddy self-attach error (`Could not initialize plugin: org.mockito.plugins.MockMaker`).

## API
### Create Voting Session
`POST /voting-sessions`

Request body:
```json
{
  "time": 60,
  "idAgenda": 1
}
```

Response:
- `201 Created`
- Empty body

## OpenAPI / Swagger UI
After starting the app:
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Project Structure
- `src/main/java/com/dbserver/voting_system/application/domain/model`: domain models
- `src/main/java/com/dbserver/voting_system/application/usecase`: use case contracts
- `src/main/java/com/dbserver/voting_system/entrypoint/rest`: REST layer (controllers + DTOs)
