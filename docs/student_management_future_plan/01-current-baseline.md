# 01 — Current System Baseline (AS-IS)

## 1. Assessment basis

This baseline reflects the supplied `System-Analysis-and-Technical-vision`, `Development-guide`, `README`, and Mermaid architecture description. The supplied system analysis says it reviewed `pom.xml`, `src/main/java`, `src/main/resources`, and `src/test/java` on 2026-11-25. Re-check the actual checkout before relying on this as an exact code inventory.

## 2. Current application shape

The current application is a single Spring Boot server-side application:
- Java 17.
- Spring Boot 4.1.0 and Spring MVC.
- Thymeleaf-rendered browser pages and Bootstrap 5.3.3 loaded from jsDelivr.
- Spring Security form login and a custom `UserDetailsService`.
- Spring Data JPA / Hibernate.
- MySQL.
- Maven Wrapper configured for Maven 3.9.16.

The browser UI and JSON API use the same service, repository, JPA entity and MySQL database. The repository description does not show a separate frontend application or a microservice deployment.

## 3. Current package responsibilities

| Area | Current responsibility |
|---|---|
| `config` | Security configuration and default user initialization |
| `controller` | Login/dashboard and student browser pages |
| `rest` | Student JSON REST endpoints |
| `service` | Student service interface and implementation |
| `repository` | Student and user Spring Data repositories |
| `entity` | `Student` and `User` JPA entities |
| `security` | Database-backed user details lookup |
| `exception` | Global exception handling |
| `resources/templates` | Login, dashboard, student list/form and error views |

## 4. Existing functional capabilities

| Capability | Baseline status |
|---|---|
| Login/logout | Implemented |
| Student create/edit | Implemented |
| Student list | Implemented, fixed page size of five |
| Student delete in browser | Implemented through GET; must be corrected |
| Student name search | Implemented, with a documented pagination/view-model limitation |
| Dashboard counts | Total, Active and Inactive student counts |
| Student REST CRUD/search | Implemented, but API is public and unpaginated |
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

No roles/permissions relationship or explicit username uniqueness constraint was identified.

There are no student-to-course, student-to-guardian, student-to-academic-year or other academic relationships in the described model.

## 6. Current security and operational risks

The supplied system analysis identifies these items for remediation:
- `/api/**` is configured as public.
- CSRF is disabled while browser form login uses sessions.
- Browser student deletion uses GET.
- Current student routes do not effectively enforce `ADMIN` versus `USER` permissions.
- Development seed credentials are predictable (`admin123` and `user123`).
- Database credentials are present in checked-in local configuration.
- The global exception handler exposes exception messages in an HTML error view.
- Missing student records do not have a consistent explicit not-found response.
- Hibernate `ddl-auto=update` is used; no versioned migration framework was identified.
- SQL logging is enabled.
- The test suite is described as containing only a context-load test.

Treat these as baseline findings to verify in source before changing them.

## 7. Existing routes described in supplied analysis

### Browser routes

| Method | Path | Purpose |
|---|---|---|
| GET | `/login` | Login page |
| GET | `/dashboard` | Dashboard counts |
| GET | `/students?page={page}` | Student list |
| GET | `/students/add` | New student form |
| POST | `/students/save` | Save student |
| GET | `/students/edit/{id}` | Edit form |
| GET | `/students/delete/{id}` | Delete student (unsafe mutation method) |
| GET | `/students/search?name={name}` | Search |
| GET | `/test-error` | Deliberate error/debug route |

### JSON API

| Method | Path | Purpose |
|---|---|---|
| GET | `/api/students` | List all |
| GET | `/api/students/{id}` | Retrieve one |
| POST | `/api/students` | Create |
| PUT | `/api/students/{id}` | Update |
| DELETE | `/api/students/{id}` | Delete |
| GET | `/api/students/search?name={name}` | Search |

The supplied analysis states these API routes are currently public, return JPA entities directly, and lack pagination/versioning and a stable error schema.

## 8. Baseline constraints for future work

1. Preserve the existing working student CRUD workflow while strengthening it.
2. Do not treat the existing `course` text field as a complete academic model.
3. Avoid adding dependent modules until their master data and relationships exist.
4. Secure the existing browser and API surfaces before exposing additional data.
5. Introduce migrations before significant schema expansion.
6. Keep the application as a modular monolith initially.
7. Revalidate every baseline claim against source code before implementation.
