# CLAUDE.md

## Project Overview

This project is a backend service built with:

- Java 25
- Spring Boot
- Hexagonal Architecture
- Maven
- JPA / PostgreSQL
- REST APIs

- The goal of this project is to keep business rules isolated from frameworks and infrastructure concerns, ensuring maintainability, testability, and low coupling.
  S.O.L.I.D: The 5 principles of OOP
  S — Single Responsibility Principle
  O — Open-Closed Principle
  L — Liskov Substitution Principle
  I — Interface Segregation Principle
  D — Dependency Inversion Principle

---

## Architecture Guidelines

This project follows **Hexagonal Architecture**.

### Core principles

- The **domain** must not depend on Spring, JPA, controllers, or external frameworks.
- The **application layer** orchestrates use cases.
- The **infrastructure layer** implements external concerns such as database access, messaging, file storage, and HTTP integrations.
- The **entrypoints** expose the application through REST controllers, consumers, schedulers, etc.
- Dependencies must always point **inward**.

---

## Package Structure
Use the following package organization:
voting-system/
└── com.dbserver.voting_system
├── application
│   ├── port
│   │   ├── in
│   │   │   ├── CreateAgendaUseCase.java
│   │   │   ├── OpenVotingSessionUseCase.java
│   │   │   ├── RegisterVoteUseCase.java
│   │   │   ├── GetVotingResultUseCase.java
│   │   │   └── GetVotesByAgendaUseCase.java
│   │   └── out
│   │       ├── AgendaRepositoryPort.java
│   │       ├── VotingSessionRepositoryPort.java
│   │       ├── VoteRepositoryPort.java
│   │       └── VotingResultRepositoryPort.java
│   ├── service
│   │   ├── CreateAgendaService.java
│   │   ├── OpenVotingSessionService.java
│   │   ├── RegisterVoteService.java
│   │   ├── GetVotingResultService.java
│   │   └── GetVotesByAgendaService.java
│   └── dto
│       ├── request
│       │   ├── CreateAgendaCommand.java
│       │   ├── OpenVotingSessionCommand.java
│       │   └── RegisterVoteCommand.java
│       └── response
│           ├── AgendaResponse.java
│           ├── VotingSessionResponse.java
│           ├── VoteResponse.java
│           └── VotingResultResponse.java
│
├── domain
│   ├── model
│   │   ├── Agenda.java
│   │   ├── VotingSession.java
│   │   ├── Vote.java
│   │   └── VotingResult.java
│   ├── enum
│   │   ├── VoteValue.java
│   │   └── VotingSessionStatus.java
│   ├── exception
│   │   ├── AgendaNotFoundException.java
│   │   ├── VotingSessionClosedException.java
│   │   ├── DuplicateVoteException.java
│   │   └── VotingSessionNotFoundException.java
│   └── service
│       └── VotingResultCalculator.java
│
├── adapters
│   ├── in
│   │   └── web
│   │       ├── AgendaController.java
│   │       ├── VotingSessionController.java
│   │       ├── VoteController.java
│   │       └── VotingResultController.java
│   │
│   └── out
│       └── dynamodb
│           ├── entity
│           │   ├── AgendaItem.java
│           │   ├── VotingSessionItem.java
│           │   ├── VoteItem.java
│           │   └── VotingResultItem.java
│           ├── repository
│           │   ├── DynamoAgendaRepositoryAdapter.java
│           │   ├── DynamoVotingSessionRepositoryAdapter.java
│           │   ├── DynamoVoteRepositoryAdapter.java
│           │   └── DynamoVotingResultRepositoryAdapter.java
│           └── mapper
│               ├── AgendaDynamoMapper.java
│               ├── VotingSessionDynamoMapper.java
│               ├── VoteDynamoMapper.java
│               └── VotingResultDynamoMapper.java
│
└── config
├── DynamoDbConfig.java
├── BeanConfig.java
└── GlobalExceptionHandler.java