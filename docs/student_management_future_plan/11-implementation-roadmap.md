# 11 — Phased Implementation Roadmap

## Roadmap rules

- The phases are dependency-based, not calendar commitments.
- Confirm the target institution type (school, college/university, or both) before finalizing academic rules.
- Complete exit criteria before starting dependent modules.
- Keep each release small enough to review and deploy safely.
- Do not introduce AI/ML features or integrations.

## Phase 0 — Discovery and baseline verification

**Work**
- Inspect the actual repository and compare it with `01-current-baseline.md`.
- Confirm target institution type and initial customer/workflow.
- Inventory existing student data and database schema.
- Identify current route consumers and any API clients.
- Agree role matrix and data sensitivity.
- Select database migration tool.
- Record decisions in `13-decisions-and-open-questions.md`.

**Exit criteria**
- Baseline verified.
- MVP scope approved.
- Existing data migration risks documented.
- No unresolved critical security design question.

## Phase 1 — Security and stabilization

**Work**
- Secure API routes.
- Implement effective role/permission checks.
- Re-enable correct browser CSRF protection.
- Replace GET deletion.
- Remove production use of seed credentials and committed secrets.
- Standardize error handling and not-found behavior.
- Add DTOs for existing student API.
- Add tests for student CRUD, search, validation and access.
- Remove or isolate test-only error route.

**Exit criteria**
- Anonymous API access denied unless explicitly public.
- Role tests pass.
- Student CRUD regression tests pass.
- No sensitive exception details exposed.
- Existing student records remain intact.

## Phase 2 — Data and architecture foundation

**Work**
- Introduce versioned database migrations.
- Define module/package boundaries.
- Refactor toward constructor injection and service transaction boundaries.
- Introduce institution, department, program/course, academic year and term master data.
- Plan and execute migration from free-text student course to managed academic reference.
- Introduce student number and lifecycle history after rules are agreed.
- Add audit and document metadata foundations.

**Exit criteria**
- Fresh database can be built from migrations.
- Existing data is mapped and verified.
- Academic references are relational.
- Critical operations generate audit records.
- No uncontrolled cross-module repository access.

## Phase 3 — Student lifecycle and admissions

**Work**
- Student profile and contacts.
- Guardian relationships.
- Admission applications and review workflow.
- Enrollment conversion.
- Document checklist and uploads.
- CSV import with validation preview.
- Student/guardian access model foundation.

**Exit criteria**
- Applicant can be reviewed and converted to an enrolled student.
- Duplicate conversion is prevented.
- Student lifecycle changes are auditable.
- Import errors are reported safely.

## Phase 4 — Core academic operations

**Work**
- Class, section and subject offerings.
- Faculty profiles and assignments.
- Attendance sessions and records.
- Timetable and conflict validation.
- Examination setup and mark entry.
- Grade/result publication according to confirmed rules.

**Exit criteria**
- Enrollment drives valid class/section membership.
- Attendance cannot be duplicated for the same session/student.
- Marks are validated and permission-controlled.
- Published results follow approved revision rules.

## Phase 5 — Fees and financial workflows

**Work**
- Fee categories and structures.
- Student fee assignment.
- Invoices, receipts and payment allocations.
- Concessions/scholarships.
- Outstanding balances and reports.
- Payment provider integration after provider selection.
- Reconciliation and refund workflows.

**Exit criteria**
- Invoice balances reconcile with payment allocations.
- Duplicate payment callbacks do not duplicate credits.
- Refunds and adjustments are traceable.
- Finance permissions are tested.

## Phase 6 — Portals and communication

**Work**
- Student self-service portal.
- Parent portal with verified relationship.
- In-app notifications.
- Email/SMS integrations as selected.
- Announcements and request workflows.
- Certificate generation and verification.

**Exit criteria**
- Cross-student access tests pass.
- Notification failures are visible/retryable.
- Documents are access-controlled.
- Portal views expose only permitted information.

## Phase 7 — Institutional service modules

Select based on actual institution demand:
- Library.
- Hostel.
- Transport.
- Placement and alumni.
- LMS.
- Grievance/helpdesk.
- Inventory/procurement.
- Events and extracurricular activities.

Each module requires a separate scope, data model, permissions, API contract, tests and rollout plan.

## Phase 8 — Enterprise readiness and scale

**Work**
- Multi-campus configuration and data scoping.
- Optional multi-institution tenancy only after architecture review.
- Advanced reporting/export.
- CI/CD and deployment automation.
- Monitoring, alerting and backup restoration.
- Performance/load testing.
- Mobile application or additional clients using stable APIs.

**Exit criteria**
- Data isolation tests pass.
- Recovery objectives are documented and tested.
- Production deployment is repeatable.
- Capacity is measured against agreed usage targets.

## Suggested priority order

1. Security and stabilization.
2. Database and architecture foundation.
3. Student lifecycle and admissions.
4. Academic structure.
5. Attendance and examinations.
6. Fees and payments.
7. Staff, timetable and portals.
8. Communication and certificates.
9. Institution-specific service modules.
10. Multi-campus and scale.

Do not begin Phase 3 or later while critical Phase 1 security issues remain unresolved.
