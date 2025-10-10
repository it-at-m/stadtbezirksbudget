# ADR-13 Use-Consistent-Code-Language

## Context

Consistent naming improves readability, maintainability, and developer onboarding.
A clear language policy ensures that both the database model and the application code remain understandable for all stakeholders.

## Decision

All of the application code is written in English.
Only the entity and attribute names in the database model are written in German.

German entity and attribute names from the database are used unchanged in the code.

## Consequences

- The database remains close to the German domain terminology and is easily understood by domain experts.
- The application code follows international conventions and remains consistent for developers.
- German terms appear in the code where they reference database entities or fields.
- Developers must be familiar with the German database terminology when working with entities and queries.