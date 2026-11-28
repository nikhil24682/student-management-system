# Student Management System

A server-rendered student management application built with Java 17 and Spring Boot. Staff can sign in to view dashboard statistics and manage student records through a Thymeleaf interface. The project also exposes a JSON REST API for student records.

## Features

- Login backed by Spring Security and BCrypt password hashing
- Dashboard counts for all, active, and inactive students
- Create, list, update, and delete student records
- Search students by name (case-insensitive)
- Student listing paginated at five records per page
- Validation for required student fields and email format
- REST endpoints to create, retrieve, update, delete, and search students
- MySQL persistence through Spring Data JPA and Hibernate

## Technology

- Java 17
- Spring Boot 4.1.0
- Spring MVC, Thymeleaf, Spring Security
- Server-rendered HTML, Bootstrap 5.3.3, local CSS, and vanilla JavaScript (no separate frontend build)
- Spring Data JPA, Hibernate, and MySQL
- Jakarta Bean Validation
- Maven (Maven Wrapper included)

## Project layout

```text
src/
├── main/
│   ├── java/com/studentmanagement/
│   │   ├── config/       # Security and initial user setup
│   │   ├── controller/   # Login, dashboard, and student pages
│   │   ├── entity/       # Student and user JPA entities
│   │   ├── exception/    # MVC exception handling
│   │   ├── repository/   # Spring Data repositories
│   │   ├── rest/         # Student REST API
│   │   ├── security/     # User details service
│   │   └── service/      # Student business operations
│   └── resources/
│       ├── static/       # Local CSS and vanilla JavaScript
│       ├── templates/    # Thymeleaf pages and shared fragments
│       └── application.properties
└── test/                 # Spring application context test
docs/                     # Project documentation
infra/                    # Docker Compose and image build files
```

## Run with Docker Compose

Docker Desktop (or Docker Engine with the Compose plugin) is the only local prerequisite. Compose builds the Java application in a container and starts it with MySQL; you do not need to install Java, Maven, or MySQL on your machine.

Create a local environment file from the template and replace each placeholder with a unique strong value:

```powershell
Copy-Item infra/.env.example infra/.env
```

Build and start the services in the background from the project root. The command returns after startup instead of continuously streaming container logs:

```powershell
docker compose --env-file infra/.env -f infra/compose.yaml up --build --detach
```

Check service status with `docker compose --env-file infra/.env -f infra/compose.yaml ps`. To follow application logs when troubleshooting, run `docker compose --env-file infra/.env -f infra/compose.yaml logs --follow app` in a separate terminal. When the app is ready, open [http://localhost:8080/login](http://localhost:8080/login). On a new database, sign in with the admin username/password configured in `infra/.env`; an optional read-only account can also be bootstrapped there. These are created only when the users table is empty, and passwords are stored as BCrypt hashes.

MySQL data is kept in the `mysql_data` Docker volume across restarts. Stop and remove the containers with `docker compose --env-file infra/.env -f infra/compose.yaml down`; this preserves the database volume. To also permanently delete the database data, run `docker compose --env-file infra/.env -f infra/compose.yaml down --volumes`.

The local `infra/.env` file is git-ignored; do not commit real credentials. Before upgrading an existing database, follow the inventory and backup instructions in [Database migrations and legacy-data readiness](./docs/database-migration-and-inventory.md).

For an existing Docker database volume, keep the same MySQL database username and passwords that were used when that volume was initialized during the first upgrade. Changing values in `.env` does not rotate credentials stored inside an existing MySQL volume; rotate them separately after the app is upgraded and verified. If the app log reports `Access denied` for `sms_user`, restore the original `MYSQL_USER` and `MYSQL_PASSWORD` values in `infra/.env`, or perform a deliberate MySQL credential recovery. Do not remove the volume as a troubleshooting shortcut if its data must be retained.

## Run locally without Docker

For this option, install JDK 17 or newer and MySQL 8 or newer. The included Maven Wrapper downloads and runs the required Maven version; a separate Maven installation is not needed.

1. Clone the repository and enter the project directory:

   ```bash
   git clone https://github.com/nikhil24682/student-management-system.git
   cd student-management-system
   ```

2. Create the database:

   ```sql
   CREATE DATABASE student_management_system;
   ```

3. Configure the database connection using environment variables; credentials have no source-controlled defaults. For example, in PowerShell:

   ```powershell
   $env:SPRING_DATASOURCE_URL = "jdbc:mysql://localhost:3306/student_management_system"
   $env:SPRING_DATASOURCE_USERNAME = "your_local_user"
   $env:SPRING_DATASOURCE_PASSWORD = "your_local_password"
   $env:APP_BOOTSTRAP_ADMIN_USERNAME = "admin"
   $env:APP_BOOTSTRAP_ADMIN_PASSWORD = "choose-a-strong-password"
   ```

   The bootstrap credentials are required only when the users table is empty; they are not used to change existing accounts.

4. Start the application:

   ```bash
   ./mvnw spring-boot:run
   ```

   On Windows, use `mvnw.cmd spring-boot:run`.

5. Open [http://localhost:8080/login](http://localhost:8080/login).

Flyway applies versioned schema migrations at startup, and Hibernate validates the resulting schema without changing it. SQL statement logging is disabled by default.

## Local login accounts

On startup, if the users table is empty, the application creates the admin account configured by `APP_BOOTSTRAP_ADMIN_USERNAME` and `APP_BOOTSTRAP_ADMIN_PASSWORD`. Optionally configure both `APP_BOOTSTRAP_USER_USERNAME` and `APP_BOOTSTRAP_USER_PASSWORD` to seed a read-only `USER` account. If the table is already populated, bootstrap credentials do not modify any account. There are no built-in passwords.

`ADMIN` can manage student records; authenticated `USER` accounts have read-only access. This is the current minimum role policy, not a complete institutional permission model.

## Web routes

Web pages use form-based login. All routes other than the login page and static assets require authentication.

| Method | Route | Description |
| --- | --- | --- |
| `GET` | `/login` | Login page |
| `GET` | `/dashboard` | Student totals and active/inactive counts |
| `GET` | `/students` | Student list; accepts a zero-based `page` query parameter |
| `GET` | `/students/add` | New student form (ADMIN) |
| `POST` | `/students/save` | Save a student (ADMIN, CSRF protected) |
| `GET` | `/students/edit/{id}` | Edit form for a student (ADMIN) |
| `POST` | `/students/delete/{id}` | Delete a student (ADMIN, CSRF protected) |
| `GET` | `/students/search?name={name}` | Search students by name |
| `POST` | `/logout` | Log out |

## REST API

The student API is rooted at `/api/students`. All API endpoints require authentication; `USER` can read and `ADMIN` can create, update, and delete. The API uses the application session and enforces CSRF on state-changing methods, so clients must authenticate and submit a valid CSRF token. Responses use DTOs rather than JPA entities.

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/students` | Return all students |
| `GET` | `/api/students/{id}` | Return one student |
| `POST` | `/api/students` | Create a student (ADMIN, CSRF protected) |
| `PUT` | `/api/students/{id}` | Update a student (ADMIN, CSRF protected) |
| `DELETE` | `/api/students/{id}` | Delete a student (ADMIN, CSRF protected; returns 204) |
| `GET` | `/api/students/search?name={name}` | Search by name |

Example JSON body for `POST` or `PUT`:

```json
{
  "name": "Jane Doe",
  "email": "jane.doe@example.com",
  "phone": "9876543210",
  "gender": "Female",
  "course": "Computer Science",
  "address": "123 Main Street",
  "dateOfBirth": "2004-05-15",
  "admissionDate": "2024-07-01",
  "status": "Active"
}
```

Student name, email, phone, gender, course, address, date of birth, admission date, and status are required. The email must also have a valid format.

## Tests

Run the test suite with:

```bash
./mvnw test
```

## Security and deployment notes

The checked-in configuration is a development baseline, not a complete production deployment:

- Provide and manage database/bootstrap credentials outside source control.
- The REST API currently uses session authentication and CSRF; select a dedicated API authentication strategy before serving third-party clients.
- `USER` read-only / `ADMIN` write access is a minimal interim policy; define the institution's complete permission matrix before production.
- Configure HTTPS, secure cookie attributes, account administration, backups, monitoring, and operational access controls for the deployment environment.
- Existing databases may contain legacy development accounts; rotate them explicitly after upgrade.

See [Database migrations and legacy-data readiness](./docs/database-migration-and-inventory.md) for the existing-database preflight and recovery workflow.

## Documentation

See [`docs/`](./docs/README.md) for the documentation index. Project-specific guides can be added there.

## License

No license has been specified for this project.
