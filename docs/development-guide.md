# Development and local setup

This guide describes running the current application from a local checkout. It does not describe a production deployment; see the security and roadmap sections in [System analysis](./system-analysis.md) before exposing the application to a network.

## Prerequisites

- JDK 17 or newer, available on `PATH` as `java`.
- A MySQL 8.4-compatible server reachable from the development machine.
- Git.
- Network access on first Maven Wrapper use to download Maven and project dependencies.

The repository includes Maven Wrapper scripts, configured to use Maven 3.9.16. Flyway manages schema changes; Hibernate validates the migrated schema and does not update it.

Check the Java runtime:

```bash
java -version
```

## Get the source

```bash
git clone https://github.com/nikhil24682/student-management-system.git
cd student-management-system
```

On Linux/macOS, the wrapper is `./mvnw`. On Windows, use `mvnw.cmd`.

## Prepare the database

Create the schema/database before starting the application:

```sql
CREATE DATABASE student_management_system;
```

Set the connection and first-admin credentials using environment variables. The application has no checked-in datasource or login-password defaults.

Linux/macOS:

```bash
export SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/student_management_system'
export SPRING_DATASOURCE_USERNAME='your_local_user'
export SPRING_DATASOURCE_PASSWORD='your_local_password'
export APP_BOOTSTRAP_ADMIN_USERNAME='admin'
export APP_BOOTSTRAP_ADMIN_PASSWORD='choose-a-strong-password'
```

In PowerShell, use:

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:mysql://localhost:3306/student_management_system"
$env:SPRING_DATASOURCE_USERNAME = "your_local_user"
$env:SPRING_DATASOURCE_PASSWORD = "your_local_password"
$env:APP_BOOTSTRAP_ADMIN_USERNAME = "admin"
$env:APP_BOOTSTRAP_ADMIN_PASSWORD = "choose-a-strong-password"
```

The bootstrap username/password are used only when the `users` table is empty. Optionally set both `APP_BOOTSTRAP_USER_USERNAME` and `APP_BOOTSTRAP_USER_PASSWORD` to create a read-only account on first startup. Never commit real credentials.

For an existing database, run the read-only inventory and take/verify a backup before starting the new version. Follow [Database migrations and legacy-data readiness](./database-migration-and-inventory.md). Flyway baselines a non-empty legacy schema at version 1 and then applies later migrations; the initial version creates tables only on a fresh database.

## Run the application

From the project root:

```bash
./mvnw spring-boot:run
```

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The application uses Spring Boot's default HTTP port (8080); no server port override is present in the checked-in properties. Browse to [http://localhost:8080/login](http://localhost:8080/login).

### First-start accounts

When there are no existing user records, `DataInitializer` creates the configured `ADMIN` and, optionally, `USER` accounts. It BCrypt-hashes both passwords and never logs them. If the users table is already populated, it does not change or recreate accounts.

The interim access policy permits authenticated users to view student records; only `ADMIN` can modify them. The REST API also requires authentication and CSRF tokens for state-changing requests.

## Configuration reference

| Property | Configuration | Notes |
| --- | --- | --- |
| `spring.application.name` | `StudentManagementSystem` | Application name. |
| `SPRING_DATASOURCE_URL` | Required for local environment | MySQL JDBC URL. |
| `SPRING_DATASOURCE_USERNAME` | Required for local environment | Database user. |
| `SPRING_DATASOURCE_PASSWORD` | Required for local environment | Database password. |
| `APP_BOOTSTRAP_ADMIN_USERNAME` / `APP_BOOTSTRAP_ADMIN_PASSWORD` | Required only when no users exist | Initial admin credentials; not applied to a populated users table. |
| `spring.jpa.hibernate.ddl-auto` | `validate` | Hibernate validates schema created/updated by Flyway. |
| `spring.jpa.show-sql` | `false` | Avoids logging SQL by default. |
| `spring.flyway.baseline-on-migrate` | `true` | Baselines non-empty legacy schemas at version 1. |
| `spring.flyway.baseline-version` | `1` | Version of the existing-table baseline. |
| `spring.thymeleaf.cache` | `false` | Template caching disabled, useful during development. |

No separate `dev` or `prod` profile is present. Security rules and environment-driven first-user bootstrap are configured in Java (`config/SecurityConfig.java` and `config/DataInitializer.java`).

## Build and test

Build the project:

```bash
./mvnw clean package
```

Run the configured tests:

```bash
./mvnw test
```

The test suite includes a Spring application context-load test and focused MVC/security tests. The context test needs a reachable MySQL database and first-admin bootstrap variables if its users table is empty.

## Development workflow

1. Make changes in the existing package that owns the responsibility: controllers for HTTP orchestration, services for business behavior, repositories for persistence, entities for persisted domain fields, and templates for rendered pages.
2. Keep web/API behavior and validation consistent. Both interfaces currently bind to `Student`; any future API contract change should consider introducing request/response DTOs rather than exposing persistence entities.
3. Run focused tests during development, then `./mvnw test` and `./mvnw clean package` before proposing a change.
4. For security-sensitive changes, test unauthenticated, authenticated, role-specific, and CSRF-protected access paths.
5. Update [System analysis](./system-analysis.md) when implementation facts, routes, data fields, or security behavior change.

## Troubleshooting

| Symptom | Checks |
| --- | --- |
| Application cannot connect to MySQL | Confirm MySQL is running, the database exists, the JDBC URL/port are correct, and the configured user can connect to that database. |
| Flyway migration fails on an existing database | Run the read-only inventory script and review blank/duplicate usernames. Back up first; resolve account conflicts explicitly instead of deleting records or editing Flyway history. |
| App starts with an empty users table but no login works | Set both `APP_BOOTSTRAP_ADMIN_USERNAME` and `APP_BOOTSTRAP_ADMIN_PASSWORD` before starting. |
| Login fails after changing the local database | Confirm the account exists and is enabled. Bootstrap never changes users in a populated table. |
| Maven Wrapper cannot start | Confirm JDK 17+ is installed and the machine can reach Maven Central on its first run. |
| Changes to templates do not appear | `spring.thymeleaf.cache=false` is configured; verify the app is running the expected checkout and inspect application output for template errors. |
| Context test fails before assertions | Check database availability and credentials; the test starts the configured application context, which includes JPA and user initialization. |
