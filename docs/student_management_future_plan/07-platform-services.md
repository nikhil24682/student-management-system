# 07 — Shared Platform Services

## 1. Purpose

Provide reusable capabilities needed by multiple business modules. Shared services must remain generic enough for reuse but must not absorb module-specific business rules.

## 2. Identity and access service

Responsibilities:
- Account lifecycle.
- Password change/reset.
- Roles and permissions.
- Scoped assignments (institution, campus, department).
- Login/session controls.
- Account lock/disable policy.

Business modules own their domain records; identity owns credentials and access policy.

## 3. Audit service

Record important actions:
- Actor user ID.
- Action and resource type/ID.
- Timestamp.
- Outcome.
- Correlation/request ID.
- Changed field names or approved before/after values where appropriate.
- Reason/comment for sensitive corrections.

Do not copy passwords, tokens, full payment credentials or unnecessary personal data into audit records. Define retention and access policy.

## 4. Document service

Responsibilities:
- Document metadata and category.
- Storage provider abstraction.
- Upload validation and size limits.
- Access authorization.
- Versioning where required.
- Download audit.
- Retention and deletion policy.

Business modules decide which documents are required and who may access them. Store files outside publicly served static directories.

## 5. Notification service

Support channels selected by the institution:
- In-application notifications.
- Email provider.
- SMS gateway.
- Optional approved messaging provider.

Capabilities:
- Templates with versioning.
- Recipient resolution.
- Consent/preferences.
- Queue/retry for transient provider errors.
- Delivery status.
- Idempotency.
- Failure reporting.
- Quiet hours and rate limits where required.

Notifications should not be sent before the underlying transaction is committed. Do not include sensitive student details in message previews unless explicitly approved.

## 6. Workflow and approval service

Reusable workflow primitives:
- Request type.
- Current state.
- Allowed transitions.
- Assigned approver or group.
- Decision, comment and timestamp.
- Escalation and due date.
- Full action history.

Initial use cases:
- Admission decision.
- Attendance correction.
- Leave request.
- Fee concession.
- Certificate request.
- Grievance resolution.

Do not build a complex visual workflow designer in the first release. Implement explicit, tested workflows for the first use cases and generalize only after repeated patterns are clear.

## 7. Reporting and export service

- Permission-aware queries.
- CSV and Excel exports.
- PDF generation from approved templates.
- Date-range and filter validation.
- Large export background processing when necessary.
- Export history and audit.
- Data minimization.

Do not expose unrestricted database queries or allow users to export records outside their scope.

## 8. Scheduling and background jobs

Use scheduled tasks for:
- Due-date reminders.
- Fee reminders.
- Scheduled report generation.
- Notification retries.
- Data housekeeping under approved retention policies.

Requirements:
- Jobs must be idempotent.
- Failed jobs must be observable and retryable.
- Use locking or coordination if multiple application instances run.
- Keep business actions auditable.

## 9. Reference data and configuration

Provide managed configuration for:
- Academic year/term.
- Status values.
- Grading scales.
- Attendance thresholds.
- Fee categories.
- Document categories.
- Notification templates.
- Institution branding and contact details.

Distinguish global reference data from institution-configurable data. Never let configuration bypass validation or authorization.
