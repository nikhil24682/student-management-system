# 12 — Developer Execution Guide (Copilot / Claude Reference)

## 1. Role

When working in this repository, act as a senior Spring Boot engineer and product-minded software architect. Use this documentation as the approved direction, but always verify the current source code before making changes.

## 2. Mandatory workflow for every task

1. Read `README.md` in this plan and the relevant feature/architecture/security documents.
2. Inspect the actual code, tests, configuration and current database mappings related to the task.
3. Summarize the current behavior and proposed change before editing.
4. Identify dependencies and unresolved business rules.
5. If a business rule is unresolved, ask for clarification or record it; do not invent it.
6. Propose the smallest safe implementation plan.
7. Update schema through versioned migrations only.
8. Implement backend authorization and business rules before relying on UI restrictions.
9. Add tests for normal, invalid, unauthorized and failure cases.
10. Update API/UI documentation and this plan where needed.
11. Run relevant tests and report exact outcomes.

## 3. Non-negotiable constraints

- No AI, ML, generative AI, AI APIs, model hosting or AI-based features.
- Do not rewrite the entire application.
- Do not introduce microservices by default.
- Do not expose JPA entities as public API contracts.
- Do not bypass authorization in controllers or services.
- Do not use GET for state-changing operations.
- Do not commit secrets or real credentials.
- Do not use Hibernate schema auto-update as the production migration strategy.
- Do not delete or rename existing database fields without a data migration plan.
- Do not silently change existing API behavior.
- Do not add dependencies without explaining why they are required.
- Do not claim a feature is complete until tests and acceptance criteria pass.

## 4. Implementation conventions

- Follow existing Java/Spring conventions unless a documented refactor is approved.
- Prefer constructor injection.
- Keep controllers thin.
- Put business logic in application/domain services.
- Use DTOs and explicit validation.
- Apply transaction boundaries at use-case/service level.
- Use explicit not-found and conflict handling.
- Use consistent API response/error conventions.
- Keep module dependencies one-directional.
- Use database constraints for integrity, not only UI validation.
- Make external integration handlers idempotent.
- Keep sensitive data out of logs.

## 5. Definition of done

A feature is done only when:
- Requirements and acceptance criteria are met.
- Authorization and data scope are enforced.
- Validation and error cases are implemented.
- Database migration is included if needed.
- Unit/integration/API tests are added.
- Relevant regression tests pass.
- UI and API behavior are consistent.
- Documentation is updated.
- No secrets or unrelated refactors are included.

## 6. Required response format when using an coding agent

Before implementation, provide:
- Existing behavior discovered.
- Files/modules likely to change.
- Data model impact.
- Security/permission impact.
- Implementation steps.
- Tests to add/run.
- Unresolved questions.

After implementation, provide:
- Summary of changes.
- Files changed.
- Migration details.
- Tests executed and results.
- Known limitations.
- Documentation updated.

## 7. Work incrementally

Do not ask an agent to “build the entire ERP” in one task. Give it one bounded module or use case at a time, such as:
- Add academic year and term master data.
- Add student number with migration.
- Add guardian relationship.
- Implement admission application workflow.
- Add attendance session and record operations.

Each task must have a clear scope and acceptance criteria.
