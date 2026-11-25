# 05 — Data Model Strategy

## 1. Purpose

The current model contains only `Student` and `User` entities according to the supplied analysis. Future modules require a deliberate relational model. This document describes conceptual entities, not final database DDL.

Do not generate migrations until business rules and relationships are reviewed.

## 2. Foundational design rules

- Use stable primary keys; do not use names or emails as identifiers.
- Define foreign keys and delete behavior explicitly.
- Prefer status history for lifecycle changes that must be audited.
- Use `createdAt`, `createdBy`, `updatedAt`, and `updatedBy` where appropriate.
- Use optimistic locking (`version`) for records vulnerable to concurrent updates.
- Store money as decimal values with explicit currency; never floating-point.
- Store dates/times with deliberate timezone semantics.
- Store documents as metadata plus a storage reference; avoid large binary blobs in ordinary business tables unless justified.
- Define uniqueness within the correct scope (for example, student number within an institution).
- Use soft archive only where business/legal retention requires it; do not use soft delete indiscriminately.

## 3. Proposed conceptual domains

### Institution and academic structure
- `Institution`
- `Campus`
- `Department`
- `AcademicYear`
- `AcademicTerm`
- `Program` or `Course`
- `Class`
- `Section`
- `Subject`
- `Room`
- `TimeSlot`

### Student and admission
- `Student`
- `StudentContact` or `Guardian`
- `StudentAddress`
- `AdmissionApplication`
- `AdmissionDocument`
- `Enrollment`
- `EnrollmentStatusHistory`
- `StudentDocument`
- `StudentStatusHistory`

### Identity and authorization
- `User`
- `Role`
- `Permission`
- `UserRole`
- `RolePermission`
- Optional scoped assignment tables such as `UserCampus` or `UserDepartment`

### Staff
- `Staff`
- `FacultyProfile`
- `StaffDepartmentAssignment`
- `FacultySubjectAssignment`
- `LeaveRequest`

### Attendance
- `AttendanceSession`
- `AttendanceRecord`
- `AttendanceCorrectionRequest`

### Examination
- `Examination`
- `AssessmentComponent`
- `ExamSchedule`
- `MarkEntry`
- `GradeScale`
- `Result`
- `RevaluationRequest`

### Finance
- `FeeCategory`
- `FeeStructure`
- `StudentFeeAssignment`
- `Invoice`
- `InvoiceLine`
- `Payment`
- `PaymentAllocation`
- `Refund`
- `Scholarship`
- `Concession`
- `FinancialTransaction`

### Shared services
- `DocumentMetadata`
- `Notification`
- `NotificationDelivery`
- `AuditEvent`
- `ApprovalRequest`
- `ApprovalAction`
- `Announcement`

## 4. Key relationship concepts

```mermaid
erDiagram
  INSTITUTION ||--o{ CAMPUS : has
  CAMPUS ||--o{ DEPARTMENT : contains
  DEPARTMENT ||--o{ PROGRAM : offers
  ACADEMIC_YEAR ||--o{ ACADEMIC_TERM : contains
  PROGRAM ||--o{ ENROLLMENT : receives
  STUDENT ||--o{ ENROLLMENT : has
  STUDENT ||--o{ STUDENT_CONTACT : has
  STUDENT ||--o{ STUDENT_DOCUMENT : owns
  CLASS ||--o{ SECTION : contains
  SECTION ||--o{ ENROLLMENT : groups
  SUBJECT ||--o{ ATTENDANCE_SESSION : scheduled_for
  ATTENDANCE_SESSION ||--o{ ATTENDANCE_RECORD : records
  STUDENT ||--o{ ATTENDANCE_RECORD : receives
  EXAMINATION ||--o{ MARK_ENTRY : contains
  STUDENT ||--o{ MARK_ENTRY : receives
  STUDENT ||--o{ INVOICE : billed
  INVOICE ||--o{ INVOICE_LINE : contains
  INVOICE ||--o{ PAYMENT_ALLOCATION : settled_by
  PAYMENT ||--o{ PAYMENT_ALLOCATION : allocates
```

This is a conceptual overview. Resolve exact cardinalities and ownership before implementation.

## 5. Student model evolution

The current `Student.course` string should not become the long-term academic relationship. A student may:
- Apply to a program.
- Be admitted.
- Enroll in a program and academic year/term.
- Move between sections.
- Change program under an approved process.
- Pause, withdraw or complete studies.

Represent these as explicit relationships and history records. Keep the stable person/student identity separate from term-specific enrollment.

Do not remove the current `course` field until:
1. A target relationship is agreed.
2. Existing values are mapped to valid course records.
3. Data migration is tested.
4. Application code reads the new relationship.
5. A rollback/recovery plan exists.

## 6. Finance data rules

- Invoice and payment are different records.
- A payment may settle one or more invoice balances through allocations.
- Provider transaction references must be unique within provider scope.
- Payment callbacks must be idempotent.
- Refunds must reference original payments.
- Never store card security codes or raw payment credentials.
- Keep immutable financial transaction history; corrections should use reversal/adjustment records.

## 7. Migration sequence

1. Inventory existing records and data quality.
2. Add academic master tables.
3. Populate reference data and map existing `Student.course` values.
4. Add enrollment relationships.
5. Update application reads/writes to use the new model.
6. Verify counts and relationships.
7. Only then consider retiring legacy columns.
8. Repeat with versioned migrations for each domain.

## 8. Data governance questions

Before production use, decide:
- Student number format and uniqueness scope.
- Whether email must be unique and whether it can be shared by guardians.
- Required student and guardian fields.
- Retention and archival rules.
- Who may view addresses, birth dates, documents and financial records.
- Whether one student can have multiple simultaneous enrollments.
- How transfers, withdrawals, re-admissions and program changes work.
- Currency, tax and receipt requirements.
- Whether multi-campus or multi-institution support is required.
