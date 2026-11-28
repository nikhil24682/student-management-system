# Student Lifecycle Management — Implementation Plan

> **Status:** Proposed; no lifecycle capabilities in this plan should be treated as implemented.
>
> **Goal:** Evolve the current student CRUD feature into a reliable student registry that can track a student's institution-approved lifecycle without losing existing records or mixing a person's identity with term-specific enrollment.

## Why this is the next major feature

The future-plan roadmap places student lifecycle and admissions after security and data foundations. Student lifecycle is the best next product capability because it extends the system's existing student-record workflows and provides the core identity and history needed by later academic operations, attendance, fees, and portals.

This plan follows the **Student lifecycle and profile** specification in [06-feature-specifications](./student_management_future_plan/06-feature-specifications.md), the model guidance in [05-data-model-strategy](./student_management_future_plan/05-data-model-strategy.md), and the sequence and release gates in [11-implementation-roadmap](./student_management_future_plan/11-implementation-roadmap.md). It is deliberately split into increments; do not implement it as a single feature branch or database migration.

## Current starting point

The current [`Student`](../src/main/java/com/studentmanagement/entity/Student.java) entity stores name, email, phone, gender, free-text course, address, date of birth, admission date, and a free-text status. Browser and REST CRUD operate on this entity. There is no student number, enrollment relationship, lifecycle history, guardian/contact model, document model, admissions workflow, or versioned database migration in the current implementation.

The existing student and account records are valuable data. Preserve current workflows during migration and avoid dropping or silently rewriting fields. The initial baseline had a public API and disabled browser CSRF; Part 0 now applies an interim authenticated role policy and CSRF protection. The institution-specific permission model and target-database readiness checks remain open before adding new sensitive profile data or lifecycle actions.

## Progress for Parts 0 and 1

- **Part 0 — code changes:** Implemented an interim authenticated/read-only USER and ADMIN-write policy, enabled CSRF, removed GET deletion and the test error route, added API DTOs and safe missing-record/error responses, and removed hard-coded datasource/bootstrap credentials.
- **Part 0 — operational exit gate:** Pending review of the interim permission policy and execution of backup/restore against the actual target database. No production or institution database was available to inspect or approve.
- **Part 1 — code and runbook:** Flyway migrations, a non-mutating inventory query, and migration/backup/recovery instructions are in place. Fresh-schema migration, synthetic legacy baseline/upgrade with data retention, inventory queries, and backup restoration into a separate test schema have passed in isolated MySQL 8.4 containers.
- **Part 1 — target-data exit gate:** Pending an operator/data owner running and reviewing the inventory and verified backup against the target database. The repository does not contain production student data.

## Product boundary

### Included, delivered incrementally

- One authoritative student identity/profile with a stable, institution-approved student number.
- Explicit academic enrollment context instead of treating `Student.course` as the long-term academic relationship.
- Institution-approved lifecycle states, validated transitions, and auditable history.
- Contact/guardian relationships and classified student documents, after privacy and storage rules are approved.
- Search/filtering and validated CSV import after the model is stable.
- Safe migration of existing student records and verification of the migrated data.

### Not included in this work

- Admission enquiry/application review, decisions, or applicant-to-student conversion. These form the separate Admissions capability in the feature specification.
- Attendance, examinations, fees, portals, notifications, or other downstream modules.
- Institution- or jurisdiction-specific rules that have not been approved.
- Deleting legacy data or removing `Student.course` before its values have been mapped and the cutover verified.

## Decisions required before dependent implementation

Record answers in [13-decisions-and-open-questions](./student_management_future_plan/13-decisions-and-open-questions.md) before the relevant increment begins.

| Decision | Needed for |
|---|---|
| Institution type and initial academic model (school, college/university, or both) | Academic catalog and enrollment structure |
| Student-number format, when assigned, and uniqueness scope | Student creation, imports, and duplicate detection |
| Allowed student lifecycle states and transitions, including withdrawal, transfer, completion, and re-admission | Lifecycle workflow and history |
| Whether multiple simultaneous enrollments are allowed | Enrollment cardinality and validation |
| Whether enrollment has its own lifecycle distinct from student lifecycle | Status ownership and reporting |
| Required profile, contact, and guardian fields; whether contacts can be shared | Profile and guardian forms |
| Which fields are sensitive and which roles may view/change them | Authorization and audit coverage |
| Archive, retention, and restore rules | Archive behavior and data retention |
| Document categories, size/type limits, retention, and approved storage provider | Document upload and access |
| CSV duplicate policy and whether imports are all-or-nothing or may partially succeed | Import transaction and error report |
| Academic catalog approach and mapping for existing free-text course values | Legacy-data migration |
| Database migration tool and production rollout/backup approach | Schema changes and recovery |

Do not encode tentative defaults as permanent business rules. If a decision remains open, defer the dependent increment or implement only the agreed, policy-neutral foundation.

## Delivery plan

Each part should be a separately reviewable pull request or implementation milestone with its own migration (when needed), tests, documentation update, and acceptance check. A later part depends on earlier parts only after their exit criteria pass.

### Part 0 — Readiness and security gate

**Purpose:** Confirm the actual baseline and make sure new student PII and lifecycle operations will not widen existing security risks.

**Work**

- Re-check the current source, deployed database shape, routes, and existing student data; update the AS-IS docs if needed.
- Complete the applicable Phase 0 discovery and Phase 1 security/stabilization items from the roadmap before adding sensitive fields or new lifecycle endpoints.
- Confirm authenticated access, effective role/permission checks, browser CSRF protection, safe mutation methods, consistent not-found/error behavior, and tests for the existing student CRUD/API paths.
- Confirm a recoverable database backup and verify the actual existing schema before migration. The old `ddl-auto=update` behavior has been replaced by Flyway migrations and Hibernate schema validation.
- Resolve the decisions above that block Parts 1–3.

**Exit criteria**

- The security and stabilization gate is met for both browser and API access.
- The data owner approves the lifecycle states, identifier scope, academic model, and legacy-data approach.
- The schema and migration approach has a tested recovery path.

### Part 1 — Versioned schema and legacy-data inventory

**Purpose:** Make future schema changes reproducible and understand the records that must be preserved.

**Work**

- Select and add the approved migration tool; establish a clean-database migration path.
- Inventory existing students, duplicate or missing values, current status values, and distinct `course` values. Record counts and mapping exceptions without modifying production data.
- Define a migration and rollback/recovery sequence for existing installations as well as fresh databases.
- Keep current application behavior working while introducing migrations; do not combine this foundational change with a broad student UI rewrite.

**Exit criteria**

- A fresh database can be created from versioned migrations.
- Existing records and their current values are inventoried and backed up.
- Migration and rollback/recovery have been tested against a representative copy of existing data.

### Part 2 — Minimal academic catalog and enrollment model

**Purpose:** Replace free-text academic association with explicit, historically meaningful enrollment context.

**Work**

- Implement only the academic master data needed for the institution-approved first enrollment workflow (at minimum, the approved program/course and academic period references).
- Model student identity separately from enrollment; put program, period, and any approved class/section placement on the enrollment rather than on the person record.
- Map existing `Student.course` values to catalog records. Send unknown or ambiguous values to an explicit review/mapping report; never discard or silently guess.
- Preserve the original value during the cutover. Remove or retire the legacy field only after all records are mapped, reads/writes use the new relationship, counts reconcile, and rollback is no longer needed.

**Exit criteria**

- New enrollments can only reference valid, active catalog values.
- Existing student records remain accessible and their academic associations are reconciled.
- Historical enrollment retains its academic context if catalog records later become inactive.

### Part 3 — Student registry and profile

**Purpose:** Make the student record a stable identity/profile that can support multiple academic periods.

**Work**

- Add the approved student number and enforce its uniqueness in the agreed scope at the database and service boundaries.
- Keep stable identity/profile fields separate from enrollment-specific data.
- Define which existing profile fields remain required, become optional, or need normalization; do not make unsupported assumptions about email uniqueness, gender values, phone formats, or date rules.
- Update browser forms, service operations, and API DTOs/contracts consistently; avoid exposing persistence entities as new public contract fields.
- Add authorization for viewing and changing sensitive fields.

**Exit criteria**

- Authorized staff can create, view, and update a student profile with an assigned/validated student number.
- Duplicate numbers are rejected clearly, including concurrent attempts.
- Existing records retain their identifiers and mapped values.
- Validation and authorization behavior is covered in tests.

### Part 4 — Enrollment operations and lifecycle history

**Purpose:** Track changes over time rather than overwriting a single free-text status.

**Work**

- Implement only the approved student and enrollment states and transition rules.
- Record each critical lifecycle transition with student/enrollment reference, previous and new state, effective date, actor, timestamp, and required reason where policy says so.
- Keep student-level lifecycle distinct from term/program enrollment status if the approved model requires both.
- Provide explicit operations for the approved initial transitions; do not permit arbitrary status edits through generic profile updates.
- Define archive/restore behavior and ensure archived records remain discoverable only to authorized users.

**Exit criteria**

- Invalid transitions are rejected and valid transitions are transactional.
- Critical changes are attributable and cannot be silently overwritten.
- Archived records follow the approved visibility and retention policy.
- Tests cover allowed/forbidden transitions, authorization, history, and failure rollback.

### Part 5 — Guardian/contact relationships

**Purpose:** Represent contact and guardian information without embedding multiple people in student text fields.

**Work**

- Implement the approved contact fields and relationship types in a separate relationship model.
- Define shared contact handling, verification requirements, and who may view or edit each field.
- Provide authorized staff workflows to add, update, and remove/deactivate relationships, with audit coverage for sensitive changes.

**Exit criteria**

- Contacts have explicit relationships to students and are not confused with login accounts.
- Access checks prevent unauthorized disclosure or changes.
- Shared-contact and relationship validation follows the approved policy.

### Part 6 — Student documents

**Purpose:** Track required documents without making uploaded files public or coupling files to database rows.

**Work**

- Add document metadata and a storage reference, category, ownership, upload actor/time, and applicable status.
- Integrate only the institution-approved storage provider; do not store files at predictable public URLs or commit uploads to the repository.
- Enforce authorization, type/size checks, retention/deletion rules, and safe download behavior.
- Add a document checklist only after required categories and lifecycle rules are approved.

**Exit criteria**

- Only authorized users can upload, list, and download a student's documents.
- Files are validated and stored outside public static paths.
- Document access and lifecycle actions are auditable and covered by tests.

### Part 7 — Search, filters, and CSV import

**Purpose:** Make the registry practical to operate at real student-data volumes.

**Work**

- Add paginated search/filtering by the approved student number, name, program, academic period, and lifecycle/enrollment status.
- Define a versioned CSV template and preview flow that reports row-level validation and mapping errors before applying changes.
- Implement the approved duplicate and partial/all-or-nothing policy. Make repeated submissions safe and avoid duplicate students or enrollments.
- Restrict imports to authorized staff and audit the import summary and actor.

**Exit criteria**

- Search filters compose correctly with pagination and are authorization-safe.
- Invalid input produces an actionable report and does not create unintended partial records.
- Import behavior matches the approved duplicate and transaction policy.
- Tests cover valid rows, malformed rows, duplicates, unknown catalog values, and authorization failures.

### Part 8 — Cutover and release

**Purpose:** Verify the complete lifecycle slice and safely retire legacy behavior.

**Work**

- Reconcile pre/post migration record counts, student numbers, course/program mappings, enrollment counts, and lifecycle history.
- Run end-to-end staff workflows from profile creation through approved transitions, archive, and search.
- Verify backup restoration and documented operational recovery.
- Remove obsolete legacy reads/writes only after migration reconciliation and rollback windows are accepted.
- Update the current baseline, system analysis, developer guide, API documentation, and this plan to distinguish shipped behavior from deferred work.

**Exit criteria**

- Data reconciliation is signed off by the data owner.
- Authorization, validation, migration, and end-to-end tests pass.
- No legacy academic values are lost or silently mapped.
- Release and rollback procedures are documented and exercised.

## Cross-cutting quality gates

Apply these to every part that changes application behavior or data:

- Tests for success, validation, authorization, not-found, conflict, and database failure behavior relevant to the change.
- No cross-student information exposure through IDs, search, API responses, logs, or documents.
- Database constraints protect invariants that must hold under concurrent requests.
- Migrations are repeatable in a fresh database and tested against existing data; destructive changes require explicit review and recovery.
- Keep each release compatible with the previous one where practical; document any deliberate API or UI behavior change.
- Do not mark a feature implemented in roadmap/specification docs until its exit criteria are verified.

## Recommended implementation order

1. Part 0 — readiness and security gate.
2. Part 1 — migrations and legacy-data inventory.
3. Part 2 — academic catalog and enrollment model.
4. Part 3 — student registry and profile.
5. Part 4 — lifecycle transitions and history.
6. Parts 5 and 6 — contacts and documents, separately.
7. Part 7 — search and import.
8. Part 8 — cutover and release.

If the institution does not yet have approved rules for a later part, ship the completed earlier increments and defer that part rather than guessing.

## Source references

- [Feature specifications — Student lifecycle and profile](./student_management_future_plan/06-feature-specifications.md)
- [Data model strategy](./student_management_future_plan/05-data-model-strategy.md)
- [Phased implementation roadmap](./student_management_future_plan/11-implementation-roadmap.md)
- [Decision log and open questions](./student_management_future_plan/13-decisions-and-open-questions.md)
- [Current system analysis](./system-analysis.md)
