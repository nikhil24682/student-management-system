# 06 — Feature Specifications

> These are proposed capabilities. Build only after dependencies and institution-specific rules are confirmed. “MVP” means the smallest useful first release of that module.

## 1. Student lifecycle and profile

**Goal:** Maintain one authoritative student record across the institution.

### MVP
- Create, view, update and archive student profile.
- Generate a unique student number.
- Maintain contact and guardian details.
- Record admission date and lifecycle status.
- Attach and classify required documents.
- Search and filter by student number, name, program, academic year and status.
- Import students from a validated CSV template with preview and error report.

### Later
- Student transfer, withdrawal, re-admission and alumni status.
- Student profile change requests and approvals.
- Full lifecycle history.
- Bulk promotion and section assignment.

### Acceptance criteria
- Duplicate student numbers are rejected within the agreed scope.
- Invalid imports do not partially create records without an explicit partial-import policy.
- Sensitive profile fields are permission-controlled.
- Changes to critical fields are audited.
- Archived records remain available to authorized users according to retention policy.

## 2. Admissions

**Goal:** Track an applicant from enquiry/application through decision and enrollment.

### MVP
- Admission enquiry and application record.
- Application reference number.
- Program/term selection.
- Applicant details and document checklist.
- Application statuses: Draft, Submitted, UnderReview, AdditionalInformationRequired, Approved, Rejected, Withdrawn.
- Review comments and decision history.
- Convert approved applicant into a student/enrollment without duplicate data entry.

### Later
- Public application portal.
- Application fee collection.
- Interview/entrance test scheduling.
- Merit lists and configurable eligibility rules.
- Bulk applicant import.

### Acceptance criteria
- Every status transition is validated.
- Only authorized reviewers can make decisions.
- Rejected/withdrawn applications cannot silently become enrollments.
- Conversion is transactional and idempotent.

## 3. Academic structure

**Goal:** Replace free-text academic references with managed academic master data.

### MVP
- Manage departments, programs/courses, academic years and terms.
- Manage classes, sections and subjects.
- Configure program/term subject offerings.
- Assign students to valid academic enrollments.
- Assign faculty to subjects/classes.

### Later
- Credit structures, prerequisites and elective selection.
- Curriculum versioning.
- Academic calendar and academic-year rollover.

### Acceptance criteria
- Enrollment references valid program, term and section records.
- Historical enrollment retains its original academic context.
- Deactivating a course does not erase historical student records.

## 4. Attendance

**Goal:** Record and report attendance consistently.

### MVP
- Create attendance session for class/subject/date.
- Mark Present, Absent, Late or Excused.
- Prevent duplicate attendance records for the same student/session.
- Allow authorized correction with reason and audit trail.
- Calculate attendance percentage using configurable rules.
- Provide date-range, student, class and subject reports.

### Later
- QR/barcode or ID-card scanning.
- Approved biometric-device integration.
- Parent absence notifications.
- Attendance eligibility rules for examinations.

### Acceptance criteria
- Only assigned/authorized faculty can mark attendance.
- Attendance changes are auditable.
- Percentage calculations use an approved denominator and excused-absence policy.
- Bulk marking is validated before commit.

## 5. Examinations and results

**Goal:** Manage assessments from scheduling to approved result publication.

### MVP
- Create examination and assessment components.
- Configure maximum/pass marks and weightings.
- Schedule examinations.
- Enter marks by authorized faculty.
- Validate marks against configured limits.
- Submit, review and approve marks.
- Publish results to authorized student/parent views.
- Generate a result sheet.

### Later
- Grade scales, GPA/CGPA, transcripts.
- Revaluation and supplementary exams.
- Hall tickets, seating and invigilation.
- Question bank and online assessment integration.

### Acceptance criteria
- Published results are immutable except through an authorized correction/revision workflow.
- Mark entry is restricted to assigned staff.
- Calculation rules are versioned and test-covered.
- Students cannot view unpublished results.

## 6. Fees and payments

**Goal:** Assign fees, issue invoices, record payments and report balances accurately.

### MVP
- Fee categories and fee structures by program/term.
- Assign fees to enrolled students.
- Generate invoices and receipts.
- Record cash/bank/manual payments with reference details.
- Track outstanding balances.
- Apply approved concessions and scholarships.
- Produce collection and dues reports.

### Later
- Payment gateway and UPI integration.
- Automated reconciliation.
- Installment plans, late fees and refunds.
- Tax/accounting integrations.

### Acceptance criteria
- Invoice, payment and payment allocation are separate records.
- Payment callbacks and retries are idempotent.
- Financial corrections create traceable adjustments/reversals.
- Users cannot edit or delete settled financial history without an authorized reversal process.
- Payment provider secrets are never stored in source control.

## 7. Faculty and staff

**Goal:** Maintain staff information and teaching assignments.

### MVP
- Staff profile and employee number.
- Department and designation.
- Faculty qualification and specialization.
- Subject/class allocation.
- Employment status and document records.
- Staff attendance and leave request basics.

### Later
- Workload reports, substitution management, payroll integration.
- Performance appraisal and training records.

### Acceptance criteria
- Staff records are distinct from login accounts.
- A staff member may have an account linked to their profile.
- Only authorized HR/administrators can view sensitive employment data.

## 8. Timetable and scheduling

**Goal:** Manage class, faculty and room schedules without conflicts.

### MVP
- Define working days and time slots.
- Define rooms and capacity.
- Allocate subject, faculty, class/section and room to a slot.
- Detect faculty, room and class conflicts.
- Publish timetable by role.

### Later
- Rule-based timetable generation.
- Exam timetable and room allocation.
- Substitution and schedule-change notifications.

### Acceptance criteria
- Conflicting allocations are rejected or explicitly overridden by an authorized role with reason.
- Published schedules have version/history.
- Timezone and academic calendar rules are consistent.

## 9. Student and parent portals

**Goal:** Provide secure self-service access to relevant information.

### MVP
- Student sees own profile, timetable, attendance, results and fee status.
- Parent sees only explicitly linked student(s).
- Download permitted documents.
- View announcements and notifications.
- Submit selected requests.

### Later
- Online admission, fee payment, meeting booking and assignment submission.

### Acceptance criteria
- Student cannot access another student's records by changing an ID.
- Parent access requires verified relationship and explicit linkage.
- Sensitive data is minimized in portal responses.
- Portal actions are audited.

## 10. Certificates and documents

**Goal:** Manage document requests, issuance and verification.

### MVP
- Document categories and metadata.
- Upload/download with authorization.
- Certificate request and approval.
- Generate PDF from institution-approved templates.
- Unique certificate number and issue register.

### Later
- QR verification page, digital signing provider integration, expiry/revocation.

### Acceptance criteria
- Files are not publicly accessible by predictable URL.
- File type, size and content checks are applied.
- Every issue/revoke action is audited.
- Generated certificate content is based on verified source records.

## 11. Additional institutional modules — later selection

### Library
Catalog, copies, issue/return, reservations, overdue tracking and fines.

### Hostel
Hostel/building/room/bed, allocation, occupancy, leave, visitor and hostel fee records.

### Transport
Vehicles, drivers, routes, stops, student allocation, transport fees and maintenance. GPS tracking only after provider/privacy review.

### Placement and alumni
Companies, opportunities, drives, applications, interviews, offers and alumni records.

### LMS
Courses, learning resources, assignments, submissions, evaluation and progress. Treat as an integration or separate bounded module if scope becomes large.

### Helpdesk and grievance
Ticket categories, assignment, priority, status, comments, escalation and resolution feedback.

### Inventory and procurement
Assets, stock, vendors, purchase requests, approvals, purchase orders and maintenance.

### Events and activities
Events, registrations, participation, attendance and results.

Do not start these modules until the core student, academic, identity, document and audit foundations are stable.
