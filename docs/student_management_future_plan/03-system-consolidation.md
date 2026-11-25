# 03 — System Consolidation and Technical Improvements

## 1. Objective

Strengthen and consolidate the current Spring Boot application before adding many feature modules. The goal is to reduce security risk, duplication and coupling while retaining a manageable deployment model.

## 2. Priority 0 — verify and secure the current application

Complete these before adding new business modules.

### 2.1 Authentication and authorization
- Review all current URL matchers and controller access.
- Require authentication for student data APIs.
- Define explicit permissions for viewing, creating, updating, deleting and approving records.
- Ensure API and browser routes enforce the same authorization rules.
- Keep development seed accounts out of production configuration.
- Add password reset/change and account disablement workflows only after the base security design is established.

### 2.2 CSRF and HTTP method correctness
- Re-enable CSRF protection for session-authenticated browser forms.
- Ensure Thymeleaf forms include CSRF tokens.
- Replace GET-based delete with POST or DELETE using an appropriate confirmation flow.
- Decide API authentication separately; do not disable browser CSRF globally to accommodate APIs.
- Add tests for CSRF behavior and unauthenticated requests.

### 2.3 Secrets and configuration
- Remove committed real credentials.
- Externalize database credentials and other secrets.
- Add environment-specific configuration, for example local/test/staging/production.
- Validate required production settings at startup.
- Avoid logging passwords, tokens, personal documents or payment data.

### 2.4 Error handling
- Replace raw exception-message display with safe user-facing messages.
- Add explicit not-found handling for student and future resources.
- Use consistent API error payloads and HTTP status codes.
- Log diagnostic details server-side with a correlation identifier.
- Do not expose stack traces or database errors to end users.

## 3. Priority 1 — consolidate application structure

### 3.1 Dependency injection and service boundaries
- Prefer constructor injection.
- Keep controllers responsible for HTTP orchestration only.
- Put business rules in application/domain services.
- Use repositories for persistence operations.
- Add transaction boundaries to service methods that perform multi-step changes.
- Avoid controllers calling repositories directly.

### 3.2 DTOs and validation
- Stop exposing JPA entities as public API contracts.
- Introduce request and response DTOs.
- Validate input at API and browser boundaries.
- Add domain validation in services for rules that cannot be expressed as field constraints.
- Define allowed values and date rules with stakeholders.
- Apply database constraints for true uniqueness and referential integrity.

### 3.3 Shared behavior
Create shared services for:
- Current-user context.
- Authorization checks.
- Document metadata/storage.
- Notifications.
- Audit events.
- Approval workflows.
- File export.
- Reference/master data.
- API error formatting.

Do not create a generic abstraction unless at least two real use cases need it.

## 4. Priority 2 — database lifecycle and integrity

- Introduce Flyway or Liquibase; choose one and standardize.
- Move schema changes into reviewed, versioned migration scripts.
- Stop relying on Hibernate `ddl-auto=update` for deployed environments.
- Use schema validation in production.
- Define foreign keys, unique constraints, indexes and delete behavior deliberately.
- Add transaction and rollback tests for multi-record workflows.
- Establish backup, restore and migration recovery procedures.
- Do not rename/drop existing columns without a migration and data-preservation plan.

## 5. Priority 3 — API and UI consolidation

### API
- Introduce an API version prefix, such as `/api/v1`.
- Add pagination, filtering and sorting to collection endpoints.
- Standardize status codes and error payloads.
- Add OpenAPI documentation.
- Define compatibility and deprecation policy.

### UI
- Establish shared Thymeleaf layouts, navigation, form components and validation messages.
- Move repeated inline styles into shared CSS/assets.
- Improve responsive behavior and accessibility.
- Use consistent empty, loading, success and error states.
- Ensure destructive actions require deliberate confirmation and safe HTTP methods.
- Keep user-facing terminology consistent across modules.

## 6. Priority 4 — operational readiness

- Add structured logging.
- Add health/readiness checks and application metrics.
- Add dependency vulnerability checks.
- Add automated build and test pipeline.
- Establish deployment configuration and rollback process.
- Containerize only when the target deployment environment is selected.
- Add database backup and restore verification.
- Define log retention and personal-data retention policies.

## 7. Refactoring constraints

- Preserve existing behavior unless a documented defect or security issue requires a change.
- Make small, reviewable changes.
- Add tests before or alongside risky refactors.
- Avoid a complete rewrite.
- Avoid microservices until independent scaling, team ownership or deployment needs are demonstrated.
- Keep a migration note for every database-impacting release.

## 8. Consolidation completion criteria

The foundation is ready for major feature expansion when:
- Student web and API operations are protected by tested permissions.
- Browser CSRF protection is correctly enabled.
- Destructive operations use correct HTTP methods.
- DTOs and consistent API errors exist.
- Database migrations are versioned.
- Critical student workflows have automated tests.
- Secrets are externalized.
- Logs and errors do not leak sensitive details.
- Application modules have documented boundaries.
