# 10 — Testing and Quality Strategy

## 1. Current baseline

The supplied system analysis describes one Spring Boot context-load test and no focused controller, service, repository, security, API or validation tests. Verify the current test tree before implementation.

## 2. Test pyramid

### Unit tests
- Domain validation and calculations.
- Service/use-case rules.
- Permission decisions.
- Fee balances, attendance percentages and grade calculations.
- Workflow state transitions.

### Integration tests
- Repository queries.
- Database constraints and migrations.
- Transaction rollback.
- Service-to-repository behavior.
- External provider adapters using test doubles.

### Web/API tests
- MVC form validation.
- API DTO validation and response codes.
- Pagination and filtering.
- Authentication and authorization.
- CSRF.
- Error response consistency.

### End-to-end tests
- Admission-to-enrollment.
- Student enrollment-to-attendance.
- Examination-to-result publication.
- Invoice-to-payment allocation.
- Parent access to linked student.
- Certificate request-to-issuance.

## 3. Required test cases for every feature

For each use case, cover:
1. Successful authorized action.
2. Invalid input.
3. Missing resource.
4. Duplicate/conflicting request.
5. Unauthorized role.
6. Out-of-scope resource.
7. Transaction failure/rollback.
8. Audit event creation.
9. Relevant boundary conditions.

## 4. Database and migration tests

- Apply migrations from an empty database.
- Upgrade from the previous supported schema.
- Verify existing student records survive.
- Verify foreign-key and uniqueness constraints.
- Test rollback/recovery procedure where supported.
- Use production-like database engine/version in integration tests.

## 5. Quality gates

A change is not ready to merge unless:
- Build succeeds.
- Relevant automated tests pass.
- New business rules have tests.
- Authorization tests cover new endpoints.
- No secrets are committed.
- Migration scripts are reviewed.
- API and user documentation are updated.
- Static analysis/dependency checks pass according to project policy.

## 6. Non-functional testing

As modules grow, add:
- Load tests for student search, attendance marking and report generation.
- Performance tests for large imports/exports.
- Accessibility checks for key workflows.
- Browser compatibility tests.
- Backup restoration drills.
- Security/dependency scanning.
- Notification/provider failure tests.

## 7. Test data

- Use synthetic student/person data.
- Never copy production personal data into developer/test environments without an approved anonymization process.
- Keep test fixtures deterministic.
- Avoid hard-coded shared production-like passwords.
