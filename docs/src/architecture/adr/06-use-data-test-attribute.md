# ADR-06 Use-Data-Test-Attribute

## Context

Testing in frontend requires identifiable tags.

## Decision

Tags that need to be tested have a `data-test`-attribute that uniquely identifies the tag in the tested component.

## Consequences

Slightly increased production code for unique identification.