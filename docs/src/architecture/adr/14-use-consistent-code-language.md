# ADR-14 Use-Consistent-Code-Language

## Context

Consistent naming improves readability, maintainability, and developer onboarding.
A clear language policy ensures that both the database model and the application code remain understandable for all stakeholders.

## Decision

All application code (including identifiers, comments, and business logic) is written in English.

An explicit exception exists for entity and attribute names that map directly to the German database model.
German entity and attribute names from the database are therefore referenced unchanged when the code interacts with those persisted fields.

## Consequences

- The database model remains close to the German domain terminology and is easily understood by domain experts.
- The application code follows international conventions and remains consistent for developers.
- German terms appear in the code where they reference database entities or fields.
- Developers must be familiar with the German database terminology when working with entities and queries.
