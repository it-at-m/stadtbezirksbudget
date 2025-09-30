# ADR-06 Use-Test-Data-Attribute

## Context

Testing in frontend requires identifiable HTML-tags.

## Decision

Tags that need to be tested have a `data-test`-attribute that uniquely identifies it in the tested component.

## Consequences

Slightly increased production code for unique identification.
