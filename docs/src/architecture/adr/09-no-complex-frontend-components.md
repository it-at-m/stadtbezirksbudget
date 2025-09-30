# ADR-09 No-Complex-Frontend-Components

## Context

In this context, components refer to Vue single-file components, which consist of a template, script, and styling. Separating functional logic from components
improves maintainability, reusability, and testability. When logic is tightly coupled to components, it becomes harder to reuse, test, and maintain. By moving
logic to composables or services, we promote a cleaner architecture and better separation of concerns.

## Decision

Vue components don't contain functional logic. The code within a component is limited to data binding and presentation. All other logic, apart from
data binding, is implemented in composables or services.

## Consequences

- Logic is easier to test because it can be tested independently of the components.
- Separation of concerns is improved, making the codebase more maintainable and scalable.
- Logic becomes more reusable across different components.
- Logic may need to be written more generically, as it is not coupled to specific components.
- There may be a slight increase in abstraction and initial onboarding effort for new developers.
