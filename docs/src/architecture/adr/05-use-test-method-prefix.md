# ADR-05 Use-Test-Method-Prefix

## Context

Consistent naming conventions for test methods improve readability, maintainability, and developer onboarding. Uniform test method naming makes it easier to
identify, search for, and automate test discovery in both frontend and backend projects. Inconsistent naming can lead to confusion and missed tests during
development or CI processes.

## Decision

All test methods in the backend start with the prefix `test`. Following lowerCamelCase, the method name continues with an uppercase letter.

In the frontend, all tests are defined using the `test` function from the testing framework (not `it`). The test name is a normal string describing the test
case.

## Consequences

- Code uniformity is improved, making test methods easier to find and manage.
- Automated tooling and test runners can reliably discover test methods.
- Searching for tests in the codebase becomes more straightforward.
- Method names are slightly longer due to the prefix.
