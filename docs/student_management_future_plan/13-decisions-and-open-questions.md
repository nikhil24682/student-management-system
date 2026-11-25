# 13 — Decision Log and Open Questions

## 1. Purpose

The supplied project resources establish the current technology and basic student-management scope, but they do not establish several product rules. These must be answered before implementing dependent features.

Do not silently choose defaults for institution-specific rules.

## 2. Decisions required before detailed implementation

| ID | Question | Why it matters | Proposed default pending approval |
|---|---|---|---|
| D-001 | Is the product for schools, colleges/universities, or both? | Determines class/grade vs semester/credit model, guardian needs and exam rules | Keep core concepts configurable; confirm before academic module |
| D-002 | Single institution, multiple campuses, or SaaS for multiple institutions? | Changes tenant and data-isolation design | Single institution first; avoid tenant complexity until confirmed |
| D-003 | What is the student identifier format and uniqueness scope? | Required for admissions, imports and integrations | Institution-scoped immutable identifier |
| D-004 | Can a student have multiple active programs/enrollments? | Affects enrollment model | Do not assume; confirm |
| D-005 | What are the student lifecycle states and allowed transitions? | Drives transfers, withdrawals and re-admissions | Define state machine with institution |
| D-006 | What grading model is required? | Marks, grades, GPA/CGPA and transcripts differ | Configurable grade scale after institution confirmation |
| D-007 | What attendance statuses and eligibility threshold apply? | Impacts reports and exam eligibility | Configurable; no hard-coded threshold |
| D-008 | What fee rules apply? | Installments, taxes, concessions, refunds and currencies vary | Confirm jurisdiction and institution policy |
| D-009 | Which payment gateway/bank providers are required? | Determines integration and reconciliation design | Provider-neutral adapter until selected |
| D-010 | Which notification channels are approved? | Consent, cost and provider requirements | In-app first; email/SMS after selection |
| D-011 | What document types and retention periods apply? | Privacy and storage design | Define a document policy before production |
| D-012 | Are parent accounts required, and how are guardians verified? | Controls access to minors'/students' records | Explicit verified relationship required |
| D-013 | Is payroll in scope? | Payroll has significant legal/accounting scope | Keep outside initial staff module |
| D-014 | Is LMS functionality required or should an external LMS be integrated? | Avoid duplicating a mature LMS | Decide after core academic module |
| D-015 | Which hosting environment is intended? | Determines secrets, storage, monitoring and deployment | Keep environment-neutral until selected |

## 3. Technical decisions to record

For each decision, record:
- ID and date.
- Context/problem.
- Options considered.
- Decision.
- Consequences/trade-offs.
- Owner/approver.
- Documents and modules affected.

Technical choices requiring confirmation:
- Flyway versus Liquibase.
- File storage provider.
- API authentication approach.
- OpenAPI tooling.
- Email/SMS providers.
- Payment gateway.
- Deployment platform and container strategy.
- Logging/metrics/monitoring stack.
- Whether a separate frontend/mobile client is planned.

## 4. Decision status template

```markdown
### D-XXX — Decision title
- Status: Proposed / Approved / Rejected / Superseded
- Date:
- Context:
- Options:
- Decision:
- Rationale:
- Consequences:
- Owner:
- Affected documents/modules:
```

## 5. Assumptions to avoid

- Do not assume the project is specifically a school or a university.
- Do not assume a particular grading, promotion or attendance policy.
- Do not assume fee/tax rules or currency.
- Do not assume a payment provider or messaging provider.
- Do not assume biometric or GPS tracking is permitted.
- Do not assume multi-tenancy is needed.
- Do not assume payroll or full accounting is required.
- Do not introduce AI-related capabilities.
