# ADR-02 No-Column-Annotation

## Context

To keep the codebase readable and maintainable, we aim to avoid unnecessary or redundant elements.
In previous projects, mismatches between property names and column names have led to confusion and errors.
Hibernate's default behavior in our application is to map property names directly to table column names, which supports clarity and consistency.

## Decision

Hibernate's `@Column` annotation is only used when strictly necessary, such as when the property name and the table column name differ.
In general, property and column names should always match.

Additional column definitions are managed via Flyway, not Hibernate, to ensure consistent database schema versioning.

## Consequences

- Renaming a property or column will require changes throughout the codebase (entities, DTOs, etc.), highlighting the importance of thoughtful naming from the
  start.
- Strictly matching property and column names improves traceability between code and database, making it easier to understand and maintain both.
- Consistency reduces the risk of errors and misunderstandings, as there are no differing terms for the same concept.
- If the rule is not followed, it may lead to confusion, harder debugging, and increased maintenance effort.
