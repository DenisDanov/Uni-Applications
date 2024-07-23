--liquibase formatted sql

--changeset denisdanov:0002 splitStatements:true
INSERT INTO uni_applications.roles (role_name, role_description)
VALUES ('ADMIN', 'Administrator role'),
       ('STUDENT', 'Student role');

INSERT INTO uni_applications.access_levels (access_type, access_description)
VALUES ('FULL_ACCESS', 'Full access to all resources'),
       ('READ_ONLY_ACCESS', 'Limited access to read only');

INSERT INTO uni_applications.accreditation_statuses (accreditation_type, accreditation_description)
VALUES ('ABET', 'Fully accredited program'),
       ('AACSB', 'Accreditation status pending'),
       ('LCME', 'Program not accredited');

INSERT INTO uni_applications.degree_types (degree_type, degree_description)
VALUES ('BACHELOR', 'Undergraduate degree'),
       ('MASTER', 'Graduate degree'),
       ('PHD', 'Doctorate degree');

INSERT INTO uni_applications.application_statuses (application_status, application_description)
VALUES ('PENDING', 'Application under review by admissions committee'),
       ('ACCEPTED', 'Application accepted'),
       ('DECLINED', 'Application rejected');

INSERT INTO uni_applications.faculties (faculty_name, established_on, total_number_students, description)
VALUES ('Faculty of Computer Science', '2000-01-01', 500, 'Leading in computer science education'),
       ('Faculty of Arts', '1995-03-10', 300, 'Promoting creativity and expression'),
       ('Faculty of Medicine', '1980-07-15', 800, 'Training future healthcare professionals');

INSERT INTO uni_applications.specialties_programs (starts_on, ends_on, duration_days, program_name, degree_type,
                                                   description, accreditation_type, credits_per_semester)
VALUES ('2022-09-01', '2026-06-30', 1460, 'Computer Engineering', 'BACHELOR', 'Study of hardware and software systems',
        'ABET', 30),
       ('2023-01-15', '2025-12-20', 1060, 'Fine Arts', 'BACHELOR', 'Creative arts and visual expression', 'AACSB', 25),
       ('2021-08-20', '2024-05-30', 1000, 'Medical Doctor', 'MASTER', 'Medical education and clinical training', 'LCME',
        35);

INSERT INTO uni_applications.specialties_requirements (min_grade, requirement_details,
                                                       language_proficiency_test_min_result,
                                                       standardized_test_min_result,
                                                       letter_of_recommendation_required, personal_statement_required)
VALUES (3.0, 'Minimum grade of 3.0 required', 75, 1500, TRUE, TRUE),
       (4, 'Minimum grade of 4.0 required', 60, 1300, FALSE, TRUE),
       (3.5, 'Minimum grade of 3.5 required', NULL, 1600, TRUE, TRUE);

INSERT INTO uni_applications.specialties (specialty_name, total_credits_required, employment_rate,
                                          specialty_requirement_id, specialty_program_id, faculty_id)
VALUES ('Computer Engineering', 120, 85.5, 1, 1, 1),
       ('Fine Arts', 90, 70.2, 2, 2, 2),
       ('Medical Doctor', 180, 95.0, 3, 3, 3);

INSERT INTO uni_applications.subjects (subject_name, subject_description)
VALUES ('Computer Science', 'Introduction to computer science and programming'),
       ('Drawing', 'Fundamentals of drawing and sketching'),
       ('Anatomy', 'Study of the structure of living organisms');

INSERT INTO uni_applications.specialties_subjects (specialty_id, subject_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);

INSERT INTO uni_applications.teachers (teacher_name)
VALUES ('Dr. Smith'),
       ('Prof. Johnson'),
       ('Ms. Williams');

INSERT INTO uni_applications.faculties_teachers (faculty_id, teacher_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);

INSERT INTO uni_applications.subjects_teachers (teacher_id, subject_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);

INSERT INTO uni_applications.specialties_subjects_info (specialty_id, subject_id, duration_hours)
VALUES (1, 1, 120),
       (2, 2, 90),
       (3, 3, 180);
