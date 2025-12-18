# Authorization

In the local development environment, Keycloak is used as a Docker image that mirrors the production Single Sign-On (SSO) environment. Local test users and
corresponding roles are automatically configured using keycloak migration.

## Roles

| Role                 | Description                                                                                                        |
| -------------------- | ------------------------------------------------------------------------------------------------------------------ |
| `sbb-sachbearbeiter` | Processes applications by reviewing them, ensuring compliance and communicating with applicants for clarification. |
| `sbb-system`         | Provides endpoint and interface access restricted to system accounts.                                              |

## Local users

| Username | Password | Roles                |
| -------- | -------- | -------------------- |
| `sbbsb`  | `sbbsb`  | `sbb-sachbearbeiter` |
| `sbbsys` | `sbbsys` | `sbb-system`         |
