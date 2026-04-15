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

## Local Infrastructure (Docker)
Start LocalStack and SonarQube locally:

<svg width="60" height="20" viewBox="0 0 188 152" fill="none" aria-label="LocalStack logo" role="img"><path d="M23.4772 25.4497H35.2232V-0.000116308H46.9693V50.8996H23.4772V25.4497ZM46.954 101H70.4462V126.45H46.954V101ZM70.4309 101H93.923V126.45H82.177V151.9H70.4309V101ZM93.9077 101H117.4V126.45H93.9077V101ZM117.385 101H140.877V126.45H129.131V151.9H117.385V101ZM140.861 101H164.354V126.45H140.861V101Z" fill="#3F51C7"></path><path d="M46.954 -0.000116308H70.4462V50.8996H46.954V-0.000116308ZM70.4309 -0.000116308H93.923V50.8996H70.4309V-0.000116308ZM93.9077 -0.000116308H117.4V50.8996H93.9077V-0.000116308ZM117.385 -0.000116308H140.877V50.8996H117.385V-0.000116308ZM140.861 25.4497H152.608V50.8996H140.861V25.4497ZM23.4772 49.9999H46.9693V100.9H23.4772V49.9999ZM46.954 49.9999H70.4462V100.9H46.954V49.9999ZM70.4309 49.9999H82.177V75.4497H93.923V100.9H70.4309V49.9999ZM93.9077 49.9999H117.4V100.9H93.9077V49.9999ZM117.385 49.9999H129.131V75.4497H140.877V100.9H117.385V49.9999ZM140.861 49.9999H164.354V100.9H140.861V49.9999Z" fill="#7E88EC"></path><path d="M0.000299454 75.4497H11.7464V49.9999H23.4925V100.9H0.000299454V75.4497ZM164.338 75.4497H176.084V49.9999H187.83V100.9H164.338V75.4497Z" fill="#5E6AD2"></path></svg>


Generate a LocalStack auth token:
- https://app.localstack.cloud/getting-started

```bash
export LOCALSTACK_AUTH_TOKEN='your_token_here'
docker compose up -d
```


Services:
- LocalStack: `http://localhost:4566`
- SonarQube: `http://localhost:9000`

Stop all containers:

```bash
docker compose down
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

## SonarQube Analysis
1. Open SonarQube at `http://localhost:9000` and sign in (`admin` / `admin` on first start).
2. Create a user token in **My Account > Security**.
3. Run analysis with Maven:

```bash
./mvnw clean verify sonar:sonar -Dsonar.token=YOUR_SONAR_TOKEN
```

Optional (if needed):

```bash
./mvnw sonar:sonar -Dsonar.token=YOUR_SONAR_TOKEN -Dsonar.host.url=http://localhost:9000
```

## Project Structure
- `src/main/java/com/dbserver/voting_system/application/domain/model`: domain models
- `src/main/java/com/dbserver/voting_system/application/usecase`: use case contracts
- `src/main/java/com/dbserver/voting_system/entrypoint/rest`: REST layer (controllers + DTOs)

