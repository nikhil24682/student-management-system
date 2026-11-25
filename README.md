# Student Management System

A full-stack student management application built with Java and Spring Boot. It provides secure login, student CRUD operations, searching and pagination, dashboard statistics, server-side validation, and REST API endpoints.

## Features

- Spring Security authentication with BCrypt password encoding
- Dashboard showing total, active, and inactive student counts
- Create, view, update, and delete student records
- Case-insensitive student search by name
- Paginated student listings
- Server-side form and REST request validation
- Responsive user interface built with Thymeleaf and Bootstrap
- MySQL persistence with Spring Data JPA and Hibernate
- Centralized exception handling
- REST endpoints for student operations
- Automatic creation of default application users

## Technology Stack

| Category | Technology |
| --- | --- |
| Language | Java 17 |
| Framework | Spring Boot 4.1 |
| Security | Spring Security |
| Web | Spring MVC and Thymeleaf |
| Data Access | Spring Data JPA and Hibernate |
| Database | MySQL |
| Validation | Jakarta Bean Validation |
| Frontend | HTML, CSS, Bootstrap 5 |
| Build Tool | Maven |

## Project Structure

```text
src/
├── main/
│   ├── java/com/studentmanagement/
│   │   ├── config/          # Security and initial data configuration
│   │   ├── controller/      # MVC controllers
│   │   ├── entity/          # JPA entities
│   │   ├── exception/       # Global exception handling
│   │   ├── repository/      # Database repositories
│   │   ├── rest/            # REST API controllers
│   │   ├── security/        # User details service
│   │   └── service/         # Business logic
│   └── resources/
│       ├── templates/       # Thymeleaf pages
│       └── application.properties
└── test/                    # Application tests
```

## Prerequisites

- JDK 17 or later
- Maven 3.6 or later
- MySQL 8.x or later
- A supported IDE such as IntelliJ IDEA, Eclipse, VS Code, or Spring Tool Suite

## Database Setup

Create a MySQL database:

```sql
CREATE DATABASE student_management_system;
```

The application uses the following default database settings from `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_management_system
spring.datasource.username=root
spring.datasource.password=root
```

Update these values to match your local MySQL configuration. Hibernate creates or updates the required tables automatically because `spring.jpa.hibernate.ddl-auto=update` is enabled.

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/nikhil24682/student-management-system.git
cd student-management-system
```

### 2. Configure MySQL

Create the database and update the datasource username and password in `application.properties`.

### 3. Run the application

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Alternatively, import the project into your IDE and run `StudentManagementSystemApplication`.

Open `http://localhost:8080/login` in your browser.

## Default Accounts

The application creates these accounts on the first startup when no users exist:

| Username | Password | Role |
| --- | --- | --- |
| `admin` | `admin123` | `ADMIN` |
| `user` | `user123` | `USER` |

> Change or remove the default credentials before deploying the application.

## Application Routes

| Method | Route | Description |
| --- | --- | --- |
| `GET` | `/login` | Display the login page |
| `GET` | `/dashboard` | View student statistics |
| `GET` | `/students` | List paginated students |
| `GET` | `/students/add` | Open the add-student form |
| `POST` | `/students/save` | Create or update a student |
| `GET` | `/students/edit/{id}` | Open an existing student for editing |
| `GET` | `/students/delete/{id}` | Delete a student |
| `GET` | `/students/search?name={name}` | Search students by name |
| `POST` | `/logout` | Log out |

## REST API

The REST API is available under `/api/students`.

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/students` | Get all students |
| `GET` | `/api/students/{id}` | Get a student by ID |
| `POST` | `/api/students` | Create a student |
| `PUT` | `/api/students/{id}` | Update a student |
| `DELETE` | `/api/students/{id}` | Delete a student |
| `GET` | `/api/students/search?name={name}` | Search students by name |

Example request body:

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

Student fields are validated before being saved. Required text fields must not be blank, the email must use a valid format, and both date fields must be provided.

## Running Tests

```bash
./mvnw test
```

## Production Considerations

Before deploying:

- Replace the default datasource credentials
- Change the default application accounts
- Use environment variables or an external secrets manager for sensitive configuration
- Disable SQL logging and enable appropriate production logging
- Configure HTTPS and an appropriate CORS policy
- Add database migrations with a tool such as Flyway or Liquibase
- Review API security rules for your deployment requirements

## Author

**A Nikhil**

## License

No license has been specified for this project.
