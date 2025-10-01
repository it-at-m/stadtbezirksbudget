# ADR-11 Use-Directory-Structure

## Context

We need to have rules for our directory-structure so that everyone knows where they have to look for certain files in the frontend and backend.

## Decision

We want the structure inside the stadtbezirksbudget-backend/src/main/java to be feature-oriented apart from
the directories common, configuration, security, theentity.
For the frontend we decided to keep the refarch-directory-structure (see refarch-template).

## Consequences

In the feature-oriented structure it may be a little more work to compare entities or controllers, etc. with each other as each (e.g.) entity will be in a different parent directory.
However, it comes in handy if you are working with one specific feature, as all necessary classes are in the same directory.