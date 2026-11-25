# Student Management System — Development Blueprint

> **Purpose:** This documentation is the implementation reference for evolving the current Student Management System into a maintainable, secure, extensible institutional management platform.
>
> **Scope:** Conventional software engineering only. This plan intentionally excludes AI/ML features and integrations.

## How to use this documentation

Read these documents in order before implementing new modules:

1. [Current baseline](01-current-baseline.md) — what exists today and the constraints confirmed in the reviewed repository.
2. [Product vision and principles](02-product-vision.md) — target product boundaries and design principles.
3. [Consolidation and technical improvements](03-system-consolidation.md) — foundational work required before major feature expansion.
4. [Target architecture](04-target-architecture.md) — recommended modular-monolith structure and module boundaries.
5. [Data model strategy](05-data-model-strategy.md) — domain entities, relationships and migration approach.
6. [Feature specifications](06-feature-specifications.md) — functional scope and acceptance criteria for proposed modules.
7. [Platform services](07-platform-services.md) — shared capabilities used by multiple modules.
8. [API and integration standards](08-api-and-integration-standards.md) — API contracts and external integrations.
9. [Security and access-control plan](09-security-and-access-control.md) — authentication, authorization, privacy and auditing.
10. [Testing and quality strategy](10-testing-and-quality.md) — quality gates and test coverage.
11. [Phased implementation roadmap](11-implementation-roadmap.md) — sequence, dependencies and exit criteria.
12. [Developer execution guide](12-developer-execution-guide.md) — instructions for Copilot/Claude and contributors.
13. [Decision log and open questions](13-decisions-and-open-questions.md) — decisions to confirm before implementation.

## Source-of-truth rules

- The current-state statements in this plan are based on the supplied repository documentation and architecture diagram reviewed on 2026-11-25.
- The proposed target state is a plan, not a claim that those capabilities already exist.
- If source code differs from this baseline, inspect the code and update `01-current-baseline.md` before implementing.
- Do not invent business rules. Record unresolved rules in `13-decisions-and-open-questions.md`.
- Do not implement every module at once. Follow the dependencies and release gates in `11-implementation-roadmap.md`.
- Do not introduce AI/ML, generative AI, predictive models, or AI-powered services.

## Product direction

The recommended direction is a **modular institutional management platform**, evolved progressively from the existing Spring Boot monolith. Build a strong Student Information System first, then add academic operations, finance, institutional services and portals. Do not split into microservices without measured need.

## Change control

For each feature:
1. Read the relevant feature specification and its dependencies.
2. Confirm unresolved business rules.
3. Update the data model and API contract before implementation where needed.
4. Implement backend rules and authorization before UI access.
5. Add tests for success, validation, authorization and failure cases.
6. Update this documentation in the same change.
7. Mark the feature status in the relevant document only after it is implemented and verified.
