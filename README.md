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
- Spring Data JPA, Hibernate, and MySQL
- Jakarta Bean Validation
- Maven (Maven Wrapper included)
- Bootstrap 5.3.3

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
│       ├── templates/    # Thymeleaf pages
│       └── application.properties
└── test/                 # Spring application context test
docs/                     # Project documentation
```

## Requirements

- JDK 17 or newer
- MySQL 8 or newer

The included Maven Wrapper downloads and runs the required Maven version; a separate Maven installation is not needed.

## Run locally

1. Clone the repository and enter the project directory:

   ```bash
   git clone https://github.com/nikhil24682/student-management-system.git
   cd student-management-system
   ```

2. Create the database:

   ```sql
   CREATE DATABASE student_management_system;
   ```

3. Configure the database connection. The defaults in `src/main/resources/application.properties` are:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/student_management_system
   spring.datasource.username=root
   spring.datasource.password=root
   ```

   Update these values for your local MySQL account. Alternatively, Spring Boot environment variables can override them; for example, set `SPRING_DATASOURCE_USERNAME` and `SPRING_DATASOURCE_PASSWORD` before starting the application.

4. Start the application:

   ```bash
   ./mvnw spring-boot:run
   ```

   On Windows, use `mvnw.cmd spring-boot:run`.

5. Open [http://localhost:8080/login](http://localhost:8080/login).

Hibernate is configured with `spring.jpa.hibernate.ddl-auto=update`, so it creates or updates the schema at startup. SQL statements are logged in the current configuration.

## Local login accounts

On startup, if the users table is empty, the application creates these accounts:

| Username | Password | Role |
| --- | --- | --- |
| `admin` | `admin123` | `ADMIN` |
| `user` | `user123` | `USER` |

These are development defaults defined in `DataInitializer`; replace them before using the application beyond a local environment.

## Web routes

Web pages use form-based login. All routes other than the login page and static assets require authentication.

| Method | Route | Description |
| --- | --- | --- |
| `GET` | `/login` | Login page |
| `GET` | `/dashboard` | Student totals and active/inactive counts |
| `GET` | `/students` | Student list; accepts a zero-based `page` query parameter |
| `GET` | `/students/add` | New student form |
| `POST` | `/students/save` | Save a student |
| `GET` | `/students/edit/{id}` | Edit form for a student |
| `GET` | `/students/delete/{id}` | Delete a student |
| `GET` | `/students/search?name={name}` | Search students by name |
| `POST` | `/logout` | Log out |

## REST API

The student API is rooted at `/api/students`. **The current security configuration permits unauthenticated access to `/api/**`.** Do not expose this configuration to an untrusted network without first defining and applying appropriate API authentication and authorization.

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/students` | Return all students |
| `GET` | `/api/students/{id}` | Return one student |
| `POST` | `/api/students` | Create a student |
| `PUT` | `/api/students/{id}` | Update a student |
| `DELETE` | `/api/students/{id}` | Delete a student |
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

The checked-in configuration is intended for local development, not production:

- The database credentials and default login accounts are development defaults.
- The REST API is currently unauthenticated.
- CSRF protection is disabled in the Spring Security configuration.
- Hibernate schema updates and SQL logging are enabled.

Review and change these settings, configure HTTPS, and apply appropriate access controls before deployment.

## Documentation

See [`docs/`](./docs/README.md) for the documentation index. Project-specific guides can be added there.

## License

No license has been specified for this project.
