# ADR-04 Use-Test-Nested-Class

## Context

Consistent naming and structuring of nested test classes in backend tests improves readability, maintainability, and developer onboarding. Uniform nested test
class naming makes it easier to locate and understand which methods are being tested, and helps prevent missing or duplicate tests. Inconsistent structures can
lead to confusion and make it harder to map tests to source methods.

## Decision

All test classes in the backend have exactly one nested class for each method in the class under test. The nested classes are named after the method they test,
starting with a capital letter. Overloaded methods are tested in the same nested class. Each nested class contains one or more test methods.

## Consequences

- Every method in the source class is explicitly tested, improving coverage and traceability.
- The code structure is clearer, making it easier to map tests to source methods.
- Code becomes more deeply nested, which may reduce readability and increase complexity.
- Refactoring or adding new methods requires updating the test structure accordingly.
