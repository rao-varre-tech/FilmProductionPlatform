# ADR-001 - Use UUID as Primary Keys

## Status

Accepted

## Context

The platform is built using a microservices architecture.

Using auto-increment IDs can lead to collisions and makes distributed systems more difficult to scale.

## Decision

Use UUID as the primary key for every entity.

Example:

```java
@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;