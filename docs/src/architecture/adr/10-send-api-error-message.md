# ADR-10 Send-Api-Error-Message

## Context

When an error occurs in the backend, the frontend and the user often receive only a generic notification that an error occurred, without any details. This lack
of information makes it difficult for users to understand what went wrong and for developers to troubleshoot issues. Providing detailed error messages improves
user experience, enables faster support, and helps with debugging. However, care must be taken not to expose sensitive information in error messages.

## Decision

Whenever an error occurs in the backend, the error message will be sent as a string to the frontend along with an appropriate HTTP status code. Error messages
are reviewed to ensure that no sensitive or internal information is exposed to the user.

## Consequences

- Users receive more detailed information about errors, allowing them to respond appropriately.
- Developers and support teams can troubleshoot issues more efficiently.
- There is a risk of exposing sensitive or internal information if error messages are not properly reviewed and sanitized.
- Error message formats may need to be standardized for consistency and clarity.
- Implementing this approach may require changes to existing error handling logic in the backend.
