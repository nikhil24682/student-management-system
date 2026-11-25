# Development and local setup

This guide describes running the current application from a local checkout. It does not describe a production deployment; see the security and roadmap sections in [System analysis](./system-analysis.md) before exposing the application to a network.

## Prerequisites

- JDK 17 or newer, available on `PATH` as `java`.
- A MySQL server reachable from the development machine.
- Git.
- Network access on first Maven Wrapper use to download Maven and project dependencies.

The repository includes Maven Wrapper scripts, configured to use Maven 3.9.16. The project does not pin a MySQL server version in its build or application configuration; use a MySQL release compatible with the selected JDBC driver and Hibernate versions.

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

The current settings in `src/main/resources/application.properties` connect to:

```text
jdbc:mysql://localhost:3306/student_management_system
username: root
password: root
```

Change the credentials for your local environment. Spring Boot's standard external configuration can override the file values. For example, on Linux/macOS:

```bash
export SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/student_management_system'
export SPRING_DATASOURCE_USERNAME='your_local_user'
export SPRING_DATASOURCE_PASSWORD='your_local_password'
```

In PowerShell, use:

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:mysql://localhost:3306/student_management_system"
$env:SPRING_DATASOURCE_USERNAME = "your_local_user"
$env:SPRING_DATASOURCE_PASSWORD = "your_local_password"
```

Do not commit real credentials. The code configures Hibernate `ddl-auto=update`; on startup, Hibernate attempts to create/update tables. This is convenient for local development but is not a substitute for versioned migrations in deployed environments.

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

### Development accounts

`DataInitializer` creates two users only when the `users` table is empty:

| Username | Password | Role |
| --- | --- | --- |
| `admin` | `admin123` | `ADMIN` |
| `user` | `user123` | `USER` |

These are predictable local-development credentials, not suitable for a shared or production environment. If any user row already exists, the initializer creates neither account.

## Configuration reference

| Property | Checked-in value | Notes |
| --- | --- | --- |
| `spring.application.name` | `StudentManagementSystem` | Application name. |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/student_management_system` | MySQL JDBC URL. |
| `spring.datasource.username` | `root` | Local default; override outside source control. |
| `spring.datasource.password` | `root` | Local default; override outside source control. |
| `spring.jpa.hibernate.ddl-auto` | `update` | Hibernate updates the schema automatically. |
| `spring.jpa.show-sql` | `true` | Logs SQL statements. |
| `spring.jpa.properties.hibernate.format_sql` | `true` | Formats SQL output. |
| `spring.jpa.database-platform` | `org.hibernate.dialect.MySQLDialect` | Explicit Hibernate dialect. |
| `spring.thymeleaf.cache` | `false` | Template caching disabled, useful during development. |

No separate `dev` or `prod` profile is present. Security settings, seed behavior, and CSRF are configured in Java (`config/SecurityConfig.java` and `config/DataInitializer.java`), not through this properties file.

## Build and test

Build the project:

```bash
./mvnw clean package
```

Run the configured tests:

```bash
./mvnw test
```

The current test suite contains a Spring application context-load test. Since the application context configures the MySQL datasource and initializes user data through a repository, a reachable database with valid credentials is needed for the current full-context test unless a test-specific datasource/configuration is added.

## Development workflow

1. Make changes in the existing package that owns the responsibility: controllers for HTTP orchestration, services for business behavior, repositories for persistence, entities for persisted domain fields, and templates for rendered pages.
2. Keep web/API behavior and validation consistent. Both interfaces currently bind to `Student`; any future API contract change should consider introducing request/response DTOs rather than exposing persistence entities.
3. Run focused tests during development, then `./mvnw test` and `./mvnw clean package` before proposing a change.
4. For security-sensitive changes, test unauthenticated, authenticated, and role-specific access paths. The current `/api/**` rule is public and CSRF is disabled; do not treat that as a secure production baseline.
5. Update [System analysis](./system-analysis.md) when implementation facts, routes, data fields, or security behavior change.

## Troubleshooting

| Symptom | Checks |
| --- | --- |
| Application cannot connect to MySQL | Confirm MySQL is running, the database exists, the JDBC URL/port are correct, and the configured user can connect to that database. |
| Login fails after a local database change | Confirm the `users` table has a row and use the seeded credentials only when the table was initially empty. The initializer does not recreate users in a non-empty table. |
| Maven Wrapper cannot start | Confirm JDK 17+ is installed and the machine can reach Maven Central on its first run. |
| Changes to templates do not appear | `spring.thymeleaf.cache=false` is configured; verify the app is running the expected checkout and inspect application output for template errors. |
| Context test fails before assertions | Check database availability and credentials; the test starts the configured application context, which includes JPA and user initialization. |
