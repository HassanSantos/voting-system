# CLAUDE.md

## Project Overview

This project is a backend service built with:

- Java 25
- Spring Boot
- Hexagonal Architecture
- Maven
- JPA / PostgreSQL
- REST APIs

The goal of this project is to keep business rules isolated from frameworks and infrastructure concerns, ensuring maintainability, testability, and low coupling.

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

```text
com.dbserver.voting_system
├── application
│   ├── usecase
│   ├── service
│   └── port
│       ├── in
│       └── out
├── domain
│   ├── model
│   ├── exception
│   └── rule
├── infrastructure
│   ├── config
│   ├── persistence
│   │   ├── entity
│   │   ├── mapper
│   │   ├── repository
│   │   └── adapter
│   ├── client
│   ├── messaging
│   └── storage
└── entrypoint
    └── rest
        ├── controller
        ├── dto
        └── mapper