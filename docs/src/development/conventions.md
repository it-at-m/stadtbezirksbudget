# Conventions

To maintain a consistent and organized codebase, we have established the following conventions for contributing to our project. Please adhere to these
guidelines when making contributions.

## Commit messages

We use the [GitMoji](https://gitmoji.dev/) convention for commit messages. This helps us maintain clarity and consistency in our commit history.

A commit message should follow the format:

```text
<intention> [scope?][:?] <message>
```

- `<intention>`: The intention you want to express with the commit, using an emoji from the [GitMoji list](https://gitmoji.dev/). Either in the `:shortcode:` or
  Unicode format.
- `[scope?][:?]`: An **optional** string that adds contextual information for the scope of the change. If present, it should be followed by a colon.
- `<message>`: A brief explanation of the change.

Examples for possible commit messages:

- `🐛 Fix error while user image is loading`
- `✨ Add user registration dialog`
- `📝 Update README with new installation instructions`

## Branch naming

When creating branches for your contributions, please use one of the following prefixes to indicate the type of work being done. This helps in categorizing and
understanding the purpose of each branch at a glance.

<!-- markdownlint-disable MD060 -->

| Prefix      | Gitmoji | Description                                            | Example                       | Labels           |
| ----------- | ------- | ------------------------------------------------------ | ----------------------------- | ---------------- |
| `feature/`  | ✨      | Implementing a new feature or major functionality      | `feature/user-authentication` | ✨ Enhancement   |
| `fix/`      | 🐛      | Fixing a non-critical bug, issue, or regression        | `fix/ui-button-display`       | 🐛 Bug           |
| `hotfix/`   | 🚑️      | Fixing a critical issue that needs immediate resolving | `hotfix/crash-on-startup`     | 🚑️ Hotfix        |
| `docs/`     | 📝      | Documentation updates or improvements                  | `docs/api-usage-guide`        | 📝 Documentation |
| `refactor/` | ♻️      | Code refactoring without changing existing behavior    | `refactor/database-layer`     | ♻️ Refactor      |
| `ui/`       | 💄      | User interface or experience improvements              | `ui/button-alignment`         | 💄 UI/UX         |
| `security/` | 🔒️      | Fixing or improving security-related functionality     | `security/fix-token-leak`     | 🔒️ Security      |
| `chore/`    | 🛠️      | General maintenance, dependency updates, tooling, etc. | `chore/improve-logging`       | 🛠️ Maintenance   |

<!-- markdownlint-enable MD060 -->

## Labels

When creating pull requests and issues, please use the appropriate labels to categorize your changes. This helps maintainers quickly understand the nature of
your contribution. The labels should match the prefixes used in branch naming.

<!-- markdownlint-disable MD060 -->

| Label            | Description                                          | Color   |
| ---------------- | ---------------------------------------------------- | ------- |
| ✨ Enhancement   | New feature or request                               | #eeff00 |
| 🐛 Bug           | Something isn't working as intended                  | #ff4545 |
| 🚑️ Hotfix        | Critical or emergency fix                            | #b60205 |
| 🔒️ Security      | Address security vulnerabilities                     | #b60205 |
| 📌 Dependencies  | External library or package updates                  | #4da0ff |
| 📝 Documentation | Improvements or additions to documentation           | #005999 |
| ♻️ Refactor      | Code improvements without changing function          | #009600 |
| 💄 UI/UX         | Changes related to the user interface and experience | #ff2590 |
| 🛠️ Maintenance   | General maintenance and updates                      | #545454 |
| 🏗️ Backend       | Changes related to Backend                           | #ff7f00 |
| 🏗️ Frontend      | Changes related to Frontend                          | #ff7f00 |
| 🏗️ CIT-EAI       | Changes related to CIT-EAI                           | #ff7f00 |

<!-- markdownlint-disable MD060 -->
