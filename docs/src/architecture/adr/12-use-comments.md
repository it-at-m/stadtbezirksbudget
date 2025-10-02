# ADR-12 Use-Comments

## Context

Readable code is very important for every project. The aim is for good code that explains itself and thus only requires a description above methods/classes. In complex or misleading cases, comments are desired.

## Decision

Javadoc comments are written above all methods and classes. Code is to be written as readable as possible, and comments are only to be used in complex or misleading cases.

## Consequences

- There will be more code, as every method/class has a comment above it.
- Developers can quickly learn or remember what a method/class does by looking at the Javadoc comment.
