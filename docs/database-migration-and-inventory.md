# Database Migrations and Legacy-Data Readiness

## Migration behavior

Flyway owns schema creation and changes. Hibernate is set to `ddl-auto=validate`; it checks the mapped schema at startup but does not create or alter tables.

- A fresh, empty database applies all migrations under `src/main/resources/db/migration`.
- An existing non-empty database without Flyway history is automatically baselined at version `1`. `V1__initial_schema.sql` is skipped for that database, then later migrations are applied.
- `V2__enforce_unique_usernames.sql` requires every existing username to be non-empty and unique under the database collation. The migration intentionally fails rather than deleting, merging, or silently rewriting conflicting accounts.
- Back up the database and review the inventory report before starting the updated application against an existing database. The application starts Flyway before Hibernate validation.

The initial schema migration describes the current `students` and `users` tables. Treat it as the version-1 baseline; do not edit it after it has been applied. Add new schema changes as additional, forward-only versioned migrations.

## Non-mutating inventory

The inventory script reports student/user counts, distinct statuses and course strings, missing key student values, duplicate-email group counts for review (without printing email values), and blank or duplicate usernames. It does not update or export student names, contact details, dates of birth, addresses, or passwords.

For an existing Docker volume, keep the database name, user, and passwords that were used when MySQL initialized that volume. Updating Compose environment variables does not change credentials already stored in the MySQL data directory; rotate database credentials as a separate, verified operation.

With the Compose database running but before starting the updated app, run this from the repository root in PowerShell:

```powershell
Get-Content .\infra\database\student-data-inventory.sql -Raw |
  docker compose --env-file infra/.env -f infra/compose.yaml exec -T db sh -c 'MYSQL_PWD="$MYSQL_ROOT_PASSWORD" mysql --user=root "$MYSQL_DATABASE"'
```

Review the report with the database/data owner. Resolve blank or duplicate usernames through an approved account-management process before the migration; do not delete or merge users automatically. Review the distinct course values as an input to the later academic-catalog mapping work. Student field anomalies should be documented for their owners rather than guessed during this foundation migration.

## Backup and recovery

Before any migration against valuable data, create and verify a restorable backup. For a Compose-managed MySQL database, from the repository root:

```powershell
docker compose --env-file infra/.env -f infra/compose.yaml exec -T db sh -c 'MYSQL_PWD="$MYSQL_ROOT_PASSWORD" mysqldump --user=root --single-transaction --routines --triggers "$MYSQL_DATABASE"' > .\student-management-backup.sql
```

Keep the backup in an access-controlled location outside the repository. Confirm that the file is non-empty and test restoring it into a separate disposable database or isolated MySQL instance before relying on it. Do not test restoration over the only copy of valuable data.

Flyway migrations are forward-only in this project. A failed migration must be diagnosed and corrected with an approved repair/forward migration or by restoring the verified pre-migration backup; do not delete Flyway history or manually mark a migration successful to bypass a failure. The unique-username migration can fail on existing duplicate or blank usernames, which must be resolved and backed up before retrying.

## First-user bootstrap

For an empty database, set `APP_BOOTSTRAP_ADMIN_USERNAME` and `APP_BOOTSTRAP_ADMIN_PASSWORD` in the ignored `infra/.env` file before starting the app. An optional read-only account can be created with both `APP_BOOTSTRAP_USER_USERNAME` and `APP_BOOTSTRAP_USER_PASSWORD`. Passwords are BCrypt-hashed before storage. Bootstrap runs only if the `users` table is empty; it does not rotate or modify accounts on an existing database.

Rotate any credentials used by older development seed accounts after upgrading an existing database. The application does not identify or remove old accounts automatically.
