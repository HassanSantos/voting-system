# ADR-001: Adopt DynamoDB as the Primary Persistence Layer

## Status
Accepted

## Context

This first merge is considered an MVP (Minimum Viable Product). The goal of this stage is to deliver the core voting flow with a scalable and operationally simple foundation, allowing the team to validate the business process.

The system must support a workflow centered on:

- creating agendas
- opening voting sessions
- registering votes
- retrieving votes by agenda
- calculating voting results

From a technical perspective, this domain is strongly driven by well-defined access patterns and may involve a high volume of concurrent reads and writes, especially during active voting windows.


## Decision

The project adopts **Amazon DynamoDB** as the primary database.

DynamoDB is a non-relational, fully managed AWS database service chosen to support high-throughput read and write workloads while delegating operational concerns such as:

- infrastructure provisioning
- storage scaling
- availability
- replication
- patching
- failover and maintenance

This project also adopts a single-table design, where all records related to a voting agenda are stored under the same partition key, allowing the system to retrieve related entities efficiently according to the domain access patterns.

## Architectural Rationale

### 1. Workload Profile

The voting system is expected to handle many write operations for vote registration , voting session status, vote listing, and result generation.

This makes DynamoDB a strong fit because it is designed for:

- horizontal scalability
- high request throughput
- predictable low-latency access patterns

### 2. Access Pattern-Oriented Modeling

The main business interactions are known in advance and revolve around the agenda as the aggregate root. The system does not primarily depend on complex joins or highly relational query patterns.

Typical access patterns include:

- get agenda by ID
- get voting session by agenda ID
- save vote if not already registered
- list votes by agenda ID
- get or compute voting result by agenda ID

Since DynamoDB performs best when data is modeled around known queries, this domain aligns well with its design principles.

### 3. Reduced Operational Responsibility

This is an important decision for the MVP because it allows the team to:

- reduce infrastructure complexity
- speed up delivery
- rely on AWS for scaling and service availability

### 4. Alignment with the Current Application Architecture

The application follows a ports-and-adapters (hexagonal) architecture, where persistence details are isolated behind repository ports.

This means DynamoDB is used through infrastructure adapters rather than being coupled directly to the domain or application layers. As a result:

- business rules remain independent from the database technology
- testing becomes easier
- persistence can evolve with lower impact on core logic

## Current Data Modeling Approach

The current design uses a single DynamoDB table for multiple entity types.

### Partitioning Strategy

The table is centered around the agenda:

- `pk = AGENDA#{agendaId}`

The sort key identifies the entity stored within that aggregate:

- `sk = META` for agenda metadata
- `sk = SESSION` for the voting session
- `sk = RESULT` for the final voting result
- `sk = VOTE#CPF#{cpf}` for each vote

### Why This Works Well

This structure allows all data related to one agenda to be grouped together in the same partition, which improves query efficiency for the main use cases.

It also supports an important business rule: one vote per CPF per agenda.

Because the vote item uses a deterministic key based on the CPF, the application can use a conditional write to prevent duplicate votes. This is a simple and effective consistency mechanism for the current domain.

## Consequences

### Positive Consequences

- high scalability for concurrent reads and writes
- low operational burden due to AWS-managed infrastructure
- efficient retrieval of agenda-related data
- good fit for the current MVP access patterns
- simpler infrastructure strategy for early product validation

### Negative Consequences

- data modeling becomes more complex and must be query-driven
- broad analytical queries are less natural than in relational databases
- schema evolution requires careful planning around access patterns
- reporting and ad hoc fi