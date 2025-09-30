# ADR-07 Reduce-Testing-Of-Mapping

## Context

Testing should demonstrate the functionality of our application logic. We trust that the frameworks and libraries we use, such as Mapstruct, are thoroughly
tested and reliable. This allows us to focus our unit tests on custom logic rather than boilerplate mapping code. In previous projects, excessive testing of
simple mappings led to unnecessary maintenance and reduced focus on business logic.

## Decision

Mappers are only tested if explicit mapping logic is defined. This includes cases where:

- Default implementations are provided in interfaces
- Fields with different names are mapped using the `@Mapping` annotation
- Expressions are defined for mapping

The Mapstruct processor configuration in the `maven-compile-plugin` ensures that all fields in the target object are considered for mapping. If a field is
missing from the mapping declaration, a compilation error should occur.

## Consequences

- Fewer tests need to be written and maintained, reducing boilerplate and speeding up development.
- Developers rely more on Mapstruct's correctness and stability.
- More in-depth knowledge of Mapstruct is required, as assumptions are less frequently checked by tests.
- If Mapstruct's behavior changes or is misconfigured, issues may go unnoticed until runtime.
