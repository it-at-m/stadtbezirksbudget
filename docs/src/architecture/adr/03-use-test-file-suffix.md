# ADR-03 Use-Test-File-Suffix

## Context

Consistent naming conventions across the codebase improve readability, maintainability, and developer onboarding. Uniform test file naming makes it easier to
locate, search, and automate test discovery for both frontend and backend projects. Inconsistent naming can lead to confusion and missed tests during
development or CI processes.

## Decision

All test files and class names in the backend end with the suffix `Test`.
All test files in the frontend end with the suffix `.test` (instead of `.spec`).

## Consequences

- Code uniformity is improved, making test files easier to find and manage.
- Automated tooling and test runners can reliably discover test files.
- Searching for tests in the codebase becomes more straightforward.
- Class and file names are slightly longer due to the suffix.
