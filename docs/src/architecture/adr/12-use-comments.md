# ADR-12 Use-Comments

## Context

Readable code is very important for every project. We aim for good code that explains itself und thus only requires a description above methods/classes.
In complex or misleading cases we obviously want comments, but we believe that good code explains itself.

## Decision

We write javadoc-comments above all methods and classes. We write code as readable as possible and only use comments in complex/misleading cases.


## Consequences

There will be more code, as every method/class has a comment above it.
Developers can quickly learn/remember what a method/class does, by simply looking at the javadoc-comment.