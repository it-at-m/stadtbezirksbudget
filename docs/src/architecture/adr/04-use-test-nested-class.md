# ADR-04 Use-Test-Nested-Class

## Context

The code should have a certain uniformity. This also applies to the names of nested test classes.

## Decision

All test-classes in the backend have exactly one nested class for each method in the class that is to be tested.
The nested classes are named like the method they test, beginning with a capital letter.
Overloaded methods are tested in the same nested class. All nested classes have one or more test methods.

## Consequences

Every method in the source class is tested and the code structure improves.
The code becomes more nested and therefore harder to read.
