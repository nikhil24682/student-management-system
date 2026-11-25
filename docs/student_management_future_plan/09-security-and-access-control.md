# 09 — Security and Access Control Plan

## 1. Objective

Protect student, guardian, staff, academic and financial information. Security must be built into each module, not added after feature completion.

## 2. Authorization model

Use role-based access control with permission-level actions and optional data scope.

Example permission names:
- `student.read`
- `student.create`
- `student.update`
- `student.archive`
- `admission.review`
- `attendance.mark`
- `attendance.correct`
- `exam.marks.enter`
- `exam.result.publish`
- `fee.invoice.read`
- `fee.payment.record`
- `fee.refund.approve`
- `staff.read_sensitive`
- `report.export`

A role is a bundle of permissions. A user may also have scope restrictions such as institution, campus, department, class or assigned students.

## 3. Suggested role profiles

These are starting profiles, not immutable rules.

| Role | Typical access |
|---|---|
| Super Admin | Platform-level configuration and institution provisioning |
| Institution Admin | Institution configuration and user administration |
| Principal/Director | Read oversight reports and approve selected workflows |
| Admission Officer | Applications and admission decisions |
| HOD | Department-level academic/staff oversight |
| Faculty | Assigned classes, attendance and marks |
| Accountant | Fees, payments and finance reports |
| Librarian | Library records and circulation |
| Warden | Hostel allocation and operations |
| Transport Manager | Routes, vehicles and transport assignments |
| Placement Officer | Placement and career records |
| Student | Own permitted records and requests |
| Parent | Records of linked student(s) only |
| HR Manager | Staff records and HR workflows |

## 4. Mandatory controls

- Deny by default.
- Authenticate all non-public APIs.
- Enforce authorization at service/use-case or method level for sensitive operations.
- Verify resource ownership and data scope.
- Protect browser session forms with CSRF.
- Never perform state changes through GET.
- Encode output and validate uploads.
- Apply password hashing with a modern supported password encoder.
- Keep secrets out of source control.
- Use secure cookies and TLS in deployed environments.
- Rate-limit login and sensitive public endpoints.
- Record security-relevant events.
- Avoid exposing detailed exceptions to clients.

## 5. Sensitive data classification

At minimum, classify:
- Public institutional information.
- Internal operational information.
- Personal student/staff information.
- Sensitive documents and identity records.
- Financial records.
- Authentication secrets.

Define access, retention, export and audit rules for each category.

## 6. Parent/student access

- A student account must be linked to exactly the permitted student profile.
- A parent account must be linked to verified guardian/student relationships.
- Never trust a student ID supplied by the browser as proof of access.
- Check authorization for every resource request.
- Avoid including unrelated student records in list or search responses.

## 7. Financial controls

- Separate permissions for invoice creation, payment recording, refund approval and reconciliation.
- Require additional approval for high-value refunds or adjustments if institutional policy requires it.
- Keep transaction history immutable; use reversal/adjustment records.
- Validate external payment callbacks and prevent duplicate processing.

## 8. Security testing

Test at least:
- Anonymous user access.
- Each role's allowed and denied operations.
- Cross-student ID access.
- Cross-campus/department access where scope applies.
- CSRF protection.
- Session expiry/logout.
- Invalid/oversized file uploads.
- Mass assignment attempts.
- Duplicate payment callbacks.
- Unauthorized exports.
- Error responses for information leakage.

## 9. Governance

Before production, obtain institution/legal review for:
- Personal data collection and retention.
- Parent/guardian verification.
- Student records disclosure.
- Messaging consent.
- Payment processing.
- Biometric/GPS use, if later selected.
- Data residency and backup location.
