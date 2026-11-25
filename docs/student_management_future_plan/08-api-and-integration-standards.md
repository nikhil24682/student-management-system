# 08 — API and Integration Standards

## 1. API design goals

The current API described in the supplied analysis exposes student JPA entities, has unbounded collection responses and no version prefix. Future APIs should use stable contracts independent of persistence implementation.

## 2. Versioning and URL conventions

Use a version prefix for new contracts, for example:

```text
/api/v1/students
/api/v1/admissions
/api/v1/academic-years
/api/v1/attendance-sessions
/api/v1/examinations
/api/v1/invoices
```

Do not silently change existing endpoint behavior. Plan migration/deprecation if existing consumers are discovered.

## 3. DTO policy

- Use request DTOs for create/update.
- Use response DTOs for reads.
- Never bind public requests directly to JPA entities.
- Do not expose password hashes, internal flags or unrelated relationships.
- Validate field length, format, allowed values and cross-field rules.
- Use explicit mapping code or a selected mapping library after team approval.

## 4. Collection endpoints

Support:
- `page`
- `size` with a configured maximum.
- `sort` using an allowlist.
- Domain filters.
- Search terms with length limits.

Return metadata such as current page, page size, total elements and total pages. Avoid returning every student or transaction in a single response.

## 5. HTTP semantics

- `GET`: read only.
- `POST`: create or execute a command.
- `PUT`: replace/update a resource where semantics fit.
- `PATCH`: partial update only where carefully defined.
- `DELETE`: delete/archive where policy permits.

Use meaningful status codes, including 200, 201, 204, 400, 401, 403, 404, 409 and 422 where appropriate. Do not use success status for failed business operations.

## 6. Standard error format

Adopt one documented error structure, for example:

```json
{
  "code": "STUDENT_NOT_FOUND",
  "message": "The requested student could not be found.",
  "fieldErrors": [],
  "correlationId": "request-correlation-id"
}
```

Do not include stack traces, SQL, exception class names or secrets in API responses.

## 7. OpenAPI

- Generate or maintain OpenAPI documentation.
- Document authentication requirements.
- Document DTO schemas, status codes and error cases.
- Keep the specification synchronized with implementation.
- Add contract tests for critical endpoints.

## 8. External integration design

Potential integrations, only after requirements/provider selection:
- Payment gateway and UPI.
- Email service.
- SMS gateway.
- Institutional identity provider/SSO.
- Bank reconciliation.
- LMS or existing student information systems.
- Biometric/ID-card devices.
- GPS/transport provider.
- Accounting platform.

Use an adapter/provider interface so domain services do not depend directly on vendor SDKs.

## 9. Integration safety

- Keep credentials in secret configuration.
- Verify provider signatures for callbacks.
- Use idempotency keys.
- Validate callback amount, currency, reference and expected state.
- Record request/result metadata without storing secrets.
- Apply timeouts and bounded retries.
- Reconcile asynchronous or uncertain outcomes.
- Provide manual review for unresolved financial discrepancies.
- Define provider outage behavior.

## 10. API security

- Require authentication unless an endpoint is deliberately public.
- Enforce resource-level authorization, not just route-level authentication.
- Validate ownership and institution/campus scope.
- Rate-limit public endpoints where appropriate.
- Use TLS in deployed environments.
- Keep browser session CSRF controls separate from stateless API security decisions.
