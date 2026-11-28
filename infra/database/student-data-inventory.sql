SELECT 'students' AS inventory, COUNT(*) AS record_count
FROM students;

SELECT 'users' AS inventory, COUNT(*) AS record_count
FROM users;

SELECT status, COUNT(*) AS record_count
FROM students
GROUP BY status
ORDER BY status;

SELECT course, COUNT(*) AS record_count
FROM students
GROUP BY course
ORDER BY course;

SELECT
    COALESCE(SUM(name IS NULL OR TRIM(name) = ''), 0) AS missing_names,
    COALESCE(SUM(email IS NULL OR TRIM(email) = ''), 0) AS missing_emails,
    COALESCE(SUM(phone IS NULL OR TRIM(phone) = ''), 0) AS missing_phones,
    COALESCE(SUM(course IS NULL OR TRIM(course) = ''), 0) AS missing_courses,
    COALESCE(SUM(status IS NULL OR TRIM(status) = ''), 0) AS missing_statuses
FROM students;

SELECT COUNT(*) AS duplicate_email_groups
FROM (
    SELECT LOWER(TRIM(email))
    FROM students
    WHERE email IS NOT NULL AND TRIM(email) <> ''
    GROUP BY LOWER(TRIM(email))
    HAVING COUNT(*) > 1
) AS duplicate_emails;

SELECT LOWER(TRIM(username)) AS normalized_username, COUNT(*) AS record_count
FROM users
GROUP BY LOWER(TRIM(username))
HAVING normalized_username = ''
    OR normalized_username IS NULL
    OR COUNT(*) > 1
ORDER BY normalized_username;
