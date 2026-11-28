# GitHub Copilot Instructions — Student Management System

> **Repository-wide implementation rules.** Follow these instructions for every change in this repository. The existing project and its checked-in source code are the authority for the current implementation. Do not replace the project architecture or introduce a new technology stack.

## 1. Primary rule: extend the existing project, do not reinvent it

- First inspect the relevant existing source files, configuration, templates, entities, repositories, services, controllers, and tests before editing.
- Implement the requested change in the existing application structure and coding style.
- Prefer the smallest complete change that satisfies the requirement.
- Reuse existing classes, dependencies, configuration, conventions, and UI patterns wherever practical.
- Do not perform unrelated refactoring, renaming, formatting sweeps, package restructuring, or broad rewrites.
- Do not create a second implementation of functionality that already exists.
- Do not assume a proposed feature or architecture in planning documents is already implemented. Verify the repository before relying on it.
- If a requirement conflicts with the current design, explain the conflict and propose the smallest compatible solution before making a major architectural change.
- Do not silently make product or business-rule decisions. If a required rule is unclear, ask for clarification or clearly identify the assumption.

## 2. Technology stack — use only what this project already uses

The checked-in project documentation identifies the following current stack:

| Area | Existing technology / convention |
|---|---|
| Language/runtime | Java 17 |
| Application framework | Spring Boot 4.1.0 |
| Web layer | Spring MVC |
| Server-rendered UI | Thymeleaf |
| UI styling/components | Bootstrap 5.3.3, currently loaded from jsDelivr in templates |
| Authentication and authorization | Spring Security form login |
| User lookup | Existing custom `UserDetailsService` implementation |
| Password hashing | BCrypt through the existing Spring Security configuration |
| Persistence | Spring Data JPA and Hibernate |
| Database | MySQL, using the existing MySQL Connector/J dependency |
| Validation | Jakarta Bean Validation and the existing Spring validation support |
| Build | Existing Maven Wrapper, configured for Maven 3.9.16 |
| Tests | Existing Spring Boot test dependencies and project test conventions |

### Strict technology restrictions

- Do **not** introduce a new programming language, framework, runtime, frontend application, build system, database, ORM, or infrastructure platform.
- Do **not** add dependencies to `pom.xml` unless the user explicitly requests or approves the specific dependency.
- Do not introduce React, Angular, Vue, Node.js, TypeScript, Vite, or a separate frontend build.
- Do not introduce microservices, a separate API deployment, GraphQL, a message broker, Redis, Elasticsearch, or a cache platform.
- Do not introduce Docker, Kubernetes, cloud services, or a new deployment platform as part of an ordinary feature implementation.
- Do not introduce Flyway, Liquibase, another migration framework, an API documentation generator, or a new observability platform without explicit approval.
- Do not add an AI/LLM integration, AI service, chatbot, model API, vector database, or AI-related dependency. **AI-related technology is out of scope.**
- Do not upgrade Java, Spring Boot, Maven, Bootstrap, or any dependency version as an incidental part of a feature. Version upgrades require a separate explicit request and compatibility review.
- Do not replace Bootstrap or the existing Thymeleaf UI approach with another UI library or frontend architecture.
- If you believe a new dependency is genuinely necessary, stop and explain: the requirement, why the current stack cannot meet it, the proposed dependency, its impact, and an alternative that uses the existing stack. Wait for approval.

## 3. Architecture and package boundaries

Keep this application as a **single Spring Boot application (modular/layered monolith)**. Do not split it into services or independently deployed applications.

Follow the existing package structure and responsibilities. The current codebase includes packages/classes such as:

- `config` — application and security configuration, including `SecurityConfig` and `DataInitializer`.
- `controller` — browser/MVC request handling.
- `rest` — JSON REST endpoints.
- `service` — application and business logic, including `StudentService` and its implementation.
- `repository` — Spring Data JPA repositories.
- `entity` — JPA-persisted entities such as `Student` and `User`.
- `security` — user-details and authentication support.
- `exception` — application exception handling.
- `resources/templates` — Thymeleaf pages.

Rules:

1. Keep HTTP request handling in controllers. Controllers should coordinate work, validate input, and select a view or HTTP response; they should not become the home of business logic.
2. Put reusable business rules and workflows in the existing service layer.
3. Put database access and query definitions in repositories. Do not put SQL/database access in controllers or templates.
4. Keep JPA mapping in entity classes. Do not use entities as general-purpose UI or API response models when a small request/response DTO is more appropriate.
5. Keep browser pages in Thymeleaf and follow the existing Bootstrap styling and layout conventions.
6. Reuse the existing service/repository path where both MVC and REST interfaces need the same behavior.
7. Avoid circular dependencies and unnecessary abstractions, factories, base classes, interfaces, or generic frameworks.
8. Create a new package or class only when the responsibility is real and cannot reasonably fit an existing component.
9. Do not change existing route names, form field names, entity/table names, or public API behavior without checking callers and documenting the compatibility impact.

## 4. Implementation workflow — required for every task

Before coding:

1. Read the relevant existing classes and templates. Search for all usages of the affected methods, routes, fields, and entities.
2. Check `pom.xml` before considering any library; assume the existing dependencies must be used.
3. Check `application.properties` and the existing Java configuration before changing runtime behavior.
4. Identify whether the change affects browser MVC, REST API, persistence, security, or more than one of these.
5. State the intended files to change and keep the change focused.

While coding:

1. Follow existing Java naming, formatting, package, and dependency-injection conventions.
2. Prefer constructor injection where consistent with the surrounding code; do not introduce a new injection framework.
3. Use the existing Spring MVC, Spring Security, Spring Data JPA, Hibernate, Thymeleaf, Bootstrap, and Jakarta Validation facilities.
4. Validate user input on the server. Client-side validation may improve usability but must not replace server-side validation.
5. Keep business rules in services so browser and API entry points behave consistently.
6. Handle missing records deliberately. Do not return `null` and allow an accidental `NullPointerException` to determine behavior.
7. Use appropriate HTTP methods and status codes for REST operations. Do not implement destructive browser actions as GET requests.
8. Avoid exposing stack traces, raw exception messages, SQL details, credentials, or internal implementation details to users.
9. Do not hard-code secrets, production passwords, tokens, or environment-specific credentials.
10. Preserve existing data. Do not drop, rename, truncate, or recreate tables or columns without explicit approval and a safe data-impact plan.

After coding:

1. Add or update tests for the behavior changed, using the test dependencies and conventions already in the project.
2. Run the relevant tests, then the full test suite and build where the environment permits.
3. Review the final diff for unrelated edits, accidental dependency changes, secrets, and breaking changes.
4. Report exactly what changed, which tests/build commands were run, their results, and any checks that could not be run.

## 5. Security rules

Security improvements are allowed and encouraged, but implement them using the existing Spring Security and application stack. Do not weaken security to make a feature work.

- Keep authentication based on the existing Spring Security form-login approach unless a change is explicitly requested.
- Enforce authorization on the server for every protected browser route and API operation. Hiding a button or menu item is not authorization.
- Apply least privilege. Check the authenticated user's role/permissions before allowing sensitive actions.
- Treat the existing `/api/**` public access and globally disabled CSRF settings as known weaknesses in the reviewed baseline, not as patterns to copy.
- For session-authenticated browser forms, preserve CSRF protection and include the appropriate token in state-changing forms.
- Use POST for browser form mutations and the appropriate HTTP methods for REST mutations. Never delete or modify data through GET.
- Keep BCrypt password hashing through the existing encoder.
- Never log passwords, password hashes, session identifiers, tokens, or sensitive personal information.
- Do not expose raw exception messages or stack traces to the browser/API.
- Validate and authorize access to records by the current user's permissions; do not rely only on URL obscurity or client-supplied IDs.
- Add tests for unauthenticated access, authorized access, and denied access for relevant roles when security behavior changes.
- Do not add a new identity provider, OAuth server, JWT framework, or authentication product unless explicitly approved.

## 6. Database and persistence rules

- Continue using the existing MySQL database, MySQL Connector/J, Spring Data JPA, and Hibernate.
- Follow existing entity and repository conventions.
- Use JPA relationships only when the domain relationship is required; define ownership and fetch behavior deliberately.
- Avoid unnecessary eager loading and N+1 query patterns.
- Do not fetch entire tables for screens that should be paginated or filtered.
- Keep database changes backward-conscious and preserve existing student and user records.
- The reviewed baseline uses `spring.jpa.hibernate.ddl-auto=update`. Do not silently change this setting or introduce a migration tool. If a requested change requires a risky schema evolution, explain the risk and request approval for the database-change strategy first.
- Do not change database connection details or commit real credentials.
- Do not assume a database version that is not specified by the repository.

## 7. Browser UI rules

- Use Thymeleaf templates for server-rendered pages.
- Use the Bootstrap 5.3.3 conventions already present in the project.
- Keep pages consistent with the existing navigation, forms, tables, alerts, and layout.
- Prefer reusing existing fragments, layouts, and CSS patterns if they exist.
- Keep presentation logic in templates to a minimum; calculate business values in Java.
- Preserve form binding and validation feedback. Show field-level errors where appropriate.
- Ensure state-changing actions use forms with the correct HTTP method and CSRF token.
- Do not add a JavaScript framework or frontend package manager.
- Do not move business rules into JavaScript.

## 8. REST API rules

- Preserve the existing Spring MVC REST controller approach and URL conventions unless the requested feature requires a documented change.
- Do not expose JPA entities directly as a long-term API contract when a DTO is needed to control input/output fields.
- Validate request bodies with the existing Jakarta Validation support.
- Return suitable HTTP status codes for success, validation failure, missing records, and access denial.
- Do not return Java exception text or database details in API responses.
- Protect API endpoints with the existing Spring Security configuration and explicit authorization rules.
- Do not introduce a new API framework, GraphQL, or API gateway.

## 9. Testing and quality rules

- Use the test libraries already declared in `pom.xml`. Do not add JUnit alternatives, mocking frameworks, browser automation tools, or test platforms without approval.
- Add focused tests for changed service rules, repository queries, controller behavior, validation, and security as appropriate.
- Test both successful and failure paths, including invalid input and missing records.
- For authorization changes, test unauthenticated requests and each relevant role.
- Do not remove, disable, or weaken an existing test just to make the build pass.
- Run commands from the repository root using the checked-in Maven Wrapper:
  - Linux/macOS: `./mvnw test`
  - Windows: `.\mvnw.cmd test`
  - Linux/macOS build: `./mvnw clean package`
  - Windows build: `.\mvnw.cmd clean package`
- If a test requires MySQL, use the configured project database/test setup. Do not silently substitute another database technology.
- If tests cannot run because MySQL or another required environment is unavailable, state that clearly. Do not claim tests passed when they were not run.

## 10. Documentation and planning documents

- Treat the current source code and configuration as the truth about what is implemented.
- Use the repository's README, system analysis, and development guide as supporting documentation, but verify implementation details in code before changing them.
- Keep implemented behavior distinct from proposed or planned behavior.
- Update relevant project documentation when routes, fields, workflows, configuration, or actual system behavior change.
- Do not implement every item in a roadmap automatically. Implement only the feature or task the user has requested.
- Do not reinterpret a proposed future architecture as permission to introduce new technologies.

## 11. Prohibited actions without explicit approval

Copilot must not:

- Add or upgrade dependencies or plugin versions.
- Change Java, Spring Boot, Maven, Bootstrap, or database technology/version.
- Add a frontend framework, frontend build pipeline, or separate frontend application.
- Split the application into microservices or separate deployments.
- Add a new database, ORM, cache, message broker, search engine, migration framework, or cloud platform.
- Add AI/LLM capabilities or integrations.
- Replace Thymeleaf, Spring MVC, Spring Security, Spring Data JPA, Hibernate, or MySQL.
- Perform broad refactoring unrelated to the requested task.
- Remove existing functionality or change existing routes/contracts without checking impact.
- Make destructive database changes or remove existing records.
- Weaken authentication, authorization, CSRF, validation, or error handling to get a feature working.
- Claim that a feature, test, or build works without verifying it.

## 12. Required response format after each implementation task

At the end of each task, provide:

1. **Implemented:** concise description of the requested behavior completed.
2. **Files changed:** list of files and why each changed.
3. **Compatibility:** note any route, API, schema, or existing workflow impact.
4. **Tests run:** exact commands and actual results.
5. **Not run / remaining risks:** tests or checks not run, environment limitations, and any unresolved decisions.
6. **Dependencies:** explicitly state `No new dependencies added` when applicable. If a dependency change was approved, name it and explain why.

## 13. Final instruction

**Build on the current Student Management System. Keep Java 17, Spring Boot 4.1.0, Spring MVC, Thymeleaf, Bootstrap 5.3.3, Spring Security, Spring Data JPA, Hibernate, MySQL, Jakarta Validation, and the existing Maven Wrapper. Do not introduce anything new unless the user explicitly approves it. No AI-related technology. Make focused, secure, tested changes within the existing layered monolith.**
