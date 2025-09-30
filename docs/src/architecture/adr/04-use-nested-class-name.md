# ADR-04 Use-Nested-Class-Name

## Context

The code should have a certain uniformity. This also applies to the tests.

## Decision

All test-classes in the backend have exactly one inner class for each method in the class that is to be tested.
Overloaded methods are tested in the same nested class. All inner nested classes have one or more test methods. 

## Consequences

Every method in the source class is tested and the code structure improves.
