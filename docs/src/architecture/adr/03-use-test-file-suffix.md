# ADR-03 Use-Test-File-Suffix

## Context

Consistent naming conventions across the codebase improve readability, maintainability, and developer onboarding. Uniform test file naming makes it easier to
locate, search, and automate test discovery for both frontend and backend projects. Inconsistent naming can lead to confusion and missed tests during
development or CI processes.

## Decision

All test files are named like their corresponding implementation files, with an added suffix to indicate they are test files. In the backend, this suffix is
`Test`, while in the frontend, the suffix is `.test`.

In the frontend, the root describe block inside each test file is named after the tested file (excluding the file extension).

## Consequences

- Code uniformity is improved, making test files easier to find and manage.
- Automated tooling and test runners can reliably discover test files.
- Searching for tests in the codebase becomes more straightforward.
- Class and file names are slightly longer due to the suffix.
