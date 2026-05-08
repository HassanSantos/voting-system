# CLAUDE.md

## Project Overview

This project is a backend service built with:

- Java 25
- Spring Boot
- Hexagonal Architecture
- Maven
- REST APIs
- JUnit 5
- Mockito

## 🧠 General Rules

- Always use **JUnit 5 (Jupiter)**
- Use **Mockito** for mocking dependencies
- Do NOT use Spring context (`@SpringBootTest`) unless explicitly requested
- Prefer **pure unit tests** over integration tests
- Follow **Arrange / Act / Assert (AAA)** pattern
- Tests must be **isolated and deterministic**
- Avoid unnecessary complexity
- name test method name format(description_method_do) 

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