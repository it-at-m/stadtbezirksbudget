# ADR-11 Use-Directory-Structure

## Context

A defined directory structure is required so that all developers know where to look for certain files in the frontend and backend.

## Decision

The structure inside `stadtbezirksbudget-backend/src/main/java` is to be feature-oriented, except for common packages (e.g. `config`, `security`, `util`, etc.).
This means that all files related to a specific feature (e.g. `budget`, `user`, etc.) are in the same parent directory.

## Consequences

- In a feature-oriented structure, comparing entities or controllers can be harder since they are in separate directories.
- There is no central overview of all classes of the same type in the backend, as they are distributed across feature folders.
- However, it is helpful when working on a specific feature, as all necessary classes are in the same directory.
