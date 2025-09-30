# ADR-01 Use-Immutable-Variables

## Context

The use of immutable variables increases code safety and traceability. Mistakes due to unintended changes are avoided, and maintainability is improved. In the
past, bugs have repeatedly occurred because variables were accidentally overwritten.

## Decision

Parameters and local variables should generally be declared immutable with the `final` or `const` modifier, unless a change of reference is explicitly required.

## Consequences

- Consistent use of `final` or `const` increases code safety and makes the developer's intent clear.
- The decision to make a variable mutable must be made
  consciously.
- However, readability may suffer due to the abundance of modifiers, and the amount of code to write increases slightly.
