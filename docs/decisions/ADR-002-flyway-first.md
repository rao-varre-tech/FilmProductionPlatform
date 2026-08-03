# ADR-002 - Flyway First Database Management

## Status

Accepted

## Decision

Database schema changes are managed only through Flyway migrations.

Hibernate must never create or modify tables.

Configuration:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate