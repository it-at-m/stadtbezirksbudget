# ADR-09 No-Complex-Frontend-Components

## Context

The term components stands for the vue components, in the form of single-file components, in this context. These contain a template,
a script part and styling.

## Decision

There should be no functional logic in components. The code of a component should be limited to data binding.
The logic, apart from data binding, is implemented in composables or services.

## Consequences

The logic is easier to test because it can be tested independently of the components. The logic may need to be
written more generically because it is not coupled to the components.

