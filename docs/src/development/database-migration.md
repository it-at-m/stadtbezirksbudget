# Database Migration with Flyway

Database migrations are managed using Flyway, which allows for version control of DDL (Data Definition Language) files. These migrations are automatically applied when the backend application starts, provided there are new, unapplied migrations.

::: info
Migrations can mostly be generated using the built-in IntelliJ feature for database migrations, which can help streamline the process of creating new migration scripts.
:::

## Migration Process

To make any changes to the database schema:

1. **Create a New SQL Script**: Create a new SQL script in the migration folder located at `src/main/resources/db/migration` in the backend codebase.
  
2. **Naming Convention**: The script name must adhere to the following format:

   ```text
   V<version_number>__<description_split_with_underscores>.sql
   ```
   - **Example**: If the last migration is `V10__add_users_table.sql`, the next one should be named `V11__add_orders_table.sql`.
  
3. **Version Number Management**: 
   - The `<version_number>` should be the next available number, ensuring uniqueness across all migration scripts.
   - To maintain coordination among team members, it's essential to communicate which version numbers are being used for new migrations. 
   - Before creating a new migration, check the existing migration files to confirm the latest version number.
  
4. **Handling Naming Conflicts**: 
   - When reviewing and merging pull requests (PRs) that include migration files, be particularly vigilant for naming conflicts.
   - You can identify a naming conflict if the migration file shows as `modified` rather than `added` in the PR.

## Important Guidelines

- **Immutable Migrations**: Once a migration script has been applied to the database, it **cannot** be modified. If any errors are discovered in an applied migration, a new migration script must be created to rectify the issue.
  
- **PostgreSQL Dialect**: All SQL migration scripts must be written using the PostgreSQL dialect to ensure compatibility with the database.

## Example Workflow

1. **Identify Changes**: Determine what changes need to be made to the database schema.
2. **Check Existing Migrations**: Review the current migration files to find the latest version number.
3. **Create New Migration**: Write a migration script following the naming convention.
4. **Test Locally**: Run the application locally to ensure that the migration applies successfully.
5. **Submit for Review**: Create a pull request with changes, ensuring clear communication around the version number used.

By following these guidelines, a clean and organized migration process can be maintained in the PostgreSQL database using Flyway.