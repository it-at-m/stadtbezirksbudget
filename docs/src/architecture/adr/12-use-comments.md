# ADR-12 Use-Comments

## Context

Readable code is very important for every project. The aim is for good code that explains itself. Javadoc comments provide structural documentation for public
APIs, while inline comments are desired only in complex or misleading cases.

## Decision

Javadoc comments are written above all public methods and classes. Code is to be written as readable as possible, therefore inline comments are only to be used
in complex or misleading cases.

## Consequences

- There will be more code, as every public method/class has a comment above it.
- Developers can quickly learn or remember what a method/class does by looking at the Javadoc comment.
