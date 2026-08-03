# ADR-003 - Avoid @ManyToMany

## Status

Accepted

## Decision

Never use JPA @ManyToMany directly.

Instead create explicit join entities.

Example

User

↓

UserRole

↓

Role

Reason

- Easier auditing
- Future metadata support
- Better maintainability
- Enterprise design