# ADR-08 Enforce-Branch-Coverage

## Context

High test coverage is a key factor for ensuring software quality. To ensure a consistently high quality of our software, we want to define a minimum for test coverage. Branch coverage is a useful metric here, as it ensures that not only statements but also different execution paths in the code are tested.

## Decision

We are setting a minimum of 80% branch coverage.

Code that is difficult or impossible to test can be excluded from the coverage. Furthermore, generated code does not count towards coverage and should be excluded.

For ui components, coverage will not be considered and the UI will be tested only rudimentarily where it is dynamic (e.g., `v-if`).

## Consequences

- All new code must be written with sufficient tests to meet the coverage target.
- Developers need to be mindful of the coverage of their code and write meaningful tests.
- This increases the quality of our software and reduces the risk of regressions.
