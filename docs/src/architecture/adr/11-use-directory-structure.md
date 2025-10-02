# ADR-11 Use-Directory-Structure

## Context

A defined directory structure is required so that all developers know where to look for certain files in the frontend and backend.

## Decision

The structure in the backend and EAI is feature-oriented, except for common packages (e.g. `common`, `security`, `util`, etc.).
This means that all files related to a specific feature (e.g. `budget`, `user`, etc.) are in the same parent directory.

In the frontend, the structure is layer-oriented, meaning that all files of the same type (e.g. `components`, `services`, etc.) are in the same parent
directory. In these directories, files can be further organized by feature.

## Consequences

- In a feature-oriented structure, comparing entities or controllers can be harder since they are in separate directories.
- There is no central overview of all classes of the same type in the backend, as they are distributed across feature folders.
- However, it is helpful when working on a specific feature, as all necessary classes are in the same directory.
