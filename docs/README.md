# Student Management System documentation

This documentation describes the repository's current implementation and a proposed evolution path. Statements about the current system are based on the source code and configuration in this repository; recommendations are explicitly identified as future work.

## Documentation

- [Current system, gap analysis, and future roadmap](./system-analysis.md) — AS-IS architecture, modules, workflows, data model, routes, security and quality gaps, TO-BE recommendations, and phased priorities.
- [Phase 1 UI/UX refresh plan](./plan.md) — focused plan for visual consistency, themes, navigation, responsive layouts, and accessibility in the current browser interface.
- [Student lifecycle implementation plan](./student-lifecycle-implementation-plan.md) — staged delivery plan for the student registry, enrollment context, lifecycle history, contacts, documents, and import.
- [Database migrations and legacy-data readiness](./database-migration-and-inventory.md) — Flyway baseline behavior, a read-only student inventory, and backup/recovery steps.
- [Development and local setup guide](./development-guide.md) — prerequisites, database setup, configuration, local run/test commands, and contributor workflow.

## Status terminology

- **Implemented** — present in application code and configuration.
- **Partial / limited** — some behavior exists, but it does not cover the expected end-to-end capability or has a known limitation.
- **Missing** — no implementation was found in the repository reviewed.
- **Proposed** — a recommendation, not a feature of the current application.

## Scope and maintenance

The assessment covers `pom.xml`, `src/main/java`, `src/main/resources`, and `src/test/java` as present in this repository. It does not claim that features absent from this checkout exist elsewhere. Update the AS-IS sections when implementation changes; keep proposed work clearly marked until it ships.
