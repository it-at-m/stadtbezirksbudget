# ADR-01 Use-Immutable-Variables

## Context

Regarding immutability and readability, `final` or `const` enhances code safety and readability.

## Decision

Parameters and local variables should have the `final` or `const` modifier unless a change of reference is required.

## Consequences

The increased use of `final` or `const` can make the code less readable.
The decision to make a variable writable again has to be made consciously.
