# 01 — Current System Baseline (AS-IS)

## 1. Assessment basis

This baseline was rechecked against `pom.xml`, `src/main/java`, `src/main/resources`, `src/test/java`, and `infra/` on 2026-11-26. Re-check the actual checkout before relying on it as an exact code inventory.

## 2. Current application shape

The current application is a single Spring Boot server-side application:
- Java 17.
- Spring Boot 4.1.0 and Spring MVC.
- Thymeleaf-rendered browser pages and Bootstrap 5.3.3 loaded from jsDelivr.
- Spring Security form login and a custom `UserDetailsService`.
- Spring Data JPA / Hibernate.
- MySQL.
- Flyway schema migrations; Hibernate validates rather than updates the schema.
- Docker Compose for a local application and MySQL stack.
- Maven Wrapper configured for Maven 3.9.16.

The browser UI and JSON API use the same service, repository, JPA entity and MySQL database. The repository description does not show a separate frontend application or a microservice deployment.

## 3. Current package responsibilities

| Area | Current responsibility |
|---|---|
| `config` | Security configuration and environment-driven first-user initialization |
| `controller` | Login/dashboard and student browser pages |
| `rest` | Student JSON REST endpoints |
| `service` | Student service interface and implementation |
| `repository` | Student and user Spring Data repositories |
| `entity` | `Student` and `User` JPA entities |
| `security` | Database-backed user details lookup |
| `exception` | Browser and REST error handling |
| `resources/templates` | Login, dashboard, student list/form and error views |

## 4. Existing functional capabilities

| Capability | Baseline status |
|---|---|
| Login/logout | Implemented |
| Student create/edit | Implemented |
| Student mutation authorization | Interim `ADMIN`-write / authenticated-read policy |
| API authentication | Required for all API routes; ADMIN required for writes |
| CSRF | Enabled for session-authenticated browser and API mutations |
| Student list | Implemented, fixed page size of five |
| Student delete in browser | POST with CSRF protection |
| Student name search | Implemented, with a documented pagination/view-model limitation |
| Dashboard counts | Total, Active and Inactive student counts |
| Student REST CRUD/search | Implemented with request/response DTOs; authenticated, CSRF protected for writes, and unpaginated |
| User roles | Seeded `ADMIN` and `USER`; role-specific student permissions are not effectively applied to current routes |
| Account administration / registration / password reset | Not found in reviewed repository |
| Admissions workflow | Not found |
| Courses as managed entities / enrollment relationships | Not found; course is currently a student field |
| Attendance, examinations, grades | Not found |
| Fees and payments | Not found |
| Faculty/staff management | Not found |
| Parent/guardian management | Not found |
| Library, hostel, transport, placement | Not found |
| Audit history and reporting/export | Not found |

“Not found” means not present in the reviewed repository scope; it does not rule out external systems or unreviewed branches.

## 5. Current data entities

### Student

The supplied analysis identifies these fields:
- `id`
- `name`
- `email`
- `phone`
- `gender`
- `course`
- `address`
- `dateOfBirth`
- `admissionDate`
- `status`

Most business fields have basic Bean Validation constraints. The supplied analysis does not identify domain-level rules for phone format, date chronology, allowed gender/status values, or unique email.

### User

The supplied analysis identifies:
- `id`
- `username`
- `password`
- `role`
- `enabled`

No roles/permissions relationship is modeled; username is now non-null and unique in the database.

There are no student-to-course, student-to-guardian, student-to-academic-year or other academic relationships in the described model.

## 6. Current security and operational risks

- Remaining security and operational considerations:
- Student routes use a minimal interim policy: authenticated users can read, only ADMIN can manage student records. The full institution role matrix remains undecided.
- New accounts and datasource credentials are configured through environment variables; older seeded credentials in existing databases must still be rotated by an operator.
- Missing student records are mapped to safe 404 responses; REST validation/not-found/conflict errors use Problem Details.
- Flyway migrations are enabled with a baseline for existing databases; an inventory and backup/restore review must still be run against the target database before upgrade.
- SQL statement logging is disabled by default.
- Focused MVC/security tests have been added; service/repository and migration-upgrade automated tests remain limited.

Run the inventory and backup/restore procedure against the actual target database before applying migrations there.

## 7. Existing routes described in supplied analysis

### Browser routes

| Method | Path | Purpose |
|---|---|---|
| GET | `/login` | Login page |
| GET | `/dashboard` | Dashboard counts |
| GET | `/students?page={page}` | Student list |
| GET | `/students/add` | New student form (ADMIN) |
| POST | `/students/save` | Save student |
| GET | `/students/edit/{id}` | Edit form (ADMIN) |
| POST | `/students/delete/{id}` | Delete student (ADMIN; CSRF protected) |
| GET | `/students/search?name={name}` | Search |

### JSON API

| Method | Path | Purpose |
|---|---|---|
| GET | `/api/students` | List all |
| GET | `/api/students/{id}` | Retrieve one |
| POST | `/api/students` | Create |
| PUT | `/api/students/{id}` | Update |
| DELETE | `/api/students/{id}` | Delete |
| GET | `/api/students/search?name={name}` | Search |

All API routes require authentication; write operations require ADMIN and CSRF. API handlers use DTOs and safe Problem Details for handled errors. Pagination and versioning are still absent.

## 8. Baseline constraints for future work

1. Preserve the existing working student CRUD workflow while strengthening it.
2. Do not treat the existing `course` text field as a complete academic model.
3. Avoid adding dependent modules until their master data and relationships exist.
4. Complete institution-specific authorization design before exposing additional data.
5. Continue using versioned migrations and verify backups/upgrades before schema expansion.
6. Keep the application as a modular monolith initially.
7. Revalidate every baseline claim against source code before implementation.
