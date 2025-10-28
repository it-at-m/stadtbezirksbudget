# ADR-12 Use-Directory-Structure

## Context

A defined directory structure is required so that all developers know where to look for certain files in the frontend and backend.

## Decision

The structure in the backend and EAI is feature-oriented, except for common packages (e.g. `common`, `security`, `util`, etc.).
This means that all files related to a specific feature (e.g. `budget`, `user`, etc.) are in the same parent directory.
Inside each feature-based package, we use a type-oriented structure (e.g. `entities`, `dtos`, `repositories`, etc.).

In the frontend, the structure is layer-oriented, meaning that all files of the same type (e.g. `components`, `services`, etc.) are in the same parent
directory. In these directories, files can be further organized by feature.

## Consequences

- The feature-oriented structure in the backend promotes modularity, allowing for easier maintenance and scalability of individual features.
- Teams can work on different features concurrently without interfering with each other, reducing merge conflicts and improving collaboration.
- The type-oriented structure within each feature package helps developers quickly locate relevant files (e.g., entities, DTOs, repositories) related to a specific feature.
- The separation of concerns between the backend and frontend structures may lead to code duplication if similar logic is required in both layers but is organized differently.
- New developers might face a steeper learning curve due to the differing organizational structures between the backend and frontend, requiring time to adapt.
