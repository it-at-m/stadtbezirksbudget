# ADR-06 Use-Test-Data-Attribute

## Context

Reliable frontend testing requires HTML elements to be easily and uniquely identifiable. Using generic selectors like class or id can lead to brittle tests that
break with unrelated changes. The `data-test` attribute provides a stable and explicit way to target elements for automated tests, improving maintainability and
reducing false positives/negatives.

## Decision

All HTML tags that need to be tested have a `data-test` attribute that uniquely identifies the element within the tested component.

## Consequences

- Test reliability and maintainability are improved, as selectors are stable and explicit.
- Developers can refactor styles and structure without breaking tests, as `data-test` attributes are dedicated to testing.
- Slightly increased production code due to the addition of unique identifiers.
- There is a minor risk of leaking test attributes into production, but this is generally acceptable for most projects.
