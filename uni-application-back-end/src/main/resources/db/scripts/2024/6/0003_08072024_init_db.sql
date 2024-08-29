--liquibase formatted sql

--changeset denisdanov:0001 splitStatements:true

CREATE TABLE uni_applications.roles
(
    role_name        VARCHAR(30) PRIMARY KEY NOT NULL,
    role_description TEXT
);

CREATE TABLE uni_applications.access_levels
(
    access_type        VARCHAR(50) PRIMARY KEY NOT NULL,
    access_description TEXT
);

CREATE TABLE uni_applications.accreditation_statuses
(
    accreditation_type        VARCHAR(30) PRIMARY KEY NOT NULL,
    accreditation_description TEXT
);

CREATE TABLE uni_applications.degree_types
(
    degree_type        VARCHAR(30) PRIMARY KEY NOT NULL,
    degree_description TEXT
);

CREATE TABLE uni_applications.application_statuses
(
    application_status      VARCHAR(30) PRIMARY KEY NOT NULL,
    application_description TEXT
);

CREATE TABLE uni_applications.faculties
(
    id                    INT AUTO_INCREMENT PRIMARY KEY,
    faculty_name          VARCHAR(255) NOT NULL,
    established_on        DATE         NOT NULL,
    total_number_students INT,
    description           TEXT
);

CREATE TABLE uni_applications.specialties_programs
(
    id                   INT AUTO_INCREMENT PRIMARY KEY,
    starts_on            DATE         NOT NULL,
    ends_on              DATE         NOT NULL,
    duration_days        INT          NOT NULL,
    program_name         VARCHAR(255) NOT NULL,
    degree_type          VARCHAR(30)  NOT NULL,
    description          TEXT,
    accreditation_type   VARCHAR(30),
    credits_per_semester INT,

    FOREIGN KEY (accreditation_type) REFERENCES uni_applications.accreditation_statuses (accreditation_type),
    FOREIGN KEY (degree_type) REFERENCES uni_applications.degree_types (degree_type)
);

CREATE TABLE uni_applications.specialties_requirements
(
    id                                   INT AUTO_INCREMENT PRIMARY KEY,
    min_grade                            FLOAT   NOT NULL,
    requirement_details                  TEXT    NOT NULL,
    language_proficiency_test_min_result FLOAT,
    standardized_test_min_result         FLOAT   NOT NULL,
    letter_of_recommendation_required    BOOLEAN NOT NULL,
    personal_statement_required          BOOLEAN NOT NULL
);

CREATE TABLE uni_applications.specialties
(
    id                       INT AUTO_INCREMENT PRIMARY KEY,
    specialty_name           VARCHAR(50) NOT NULL UNIQUE,
    total_credits_required   INT,
    employment_rate          FLOAT,
    specialty_requirement_id INT         NOT NULL,
    specialty_program_id     INT         NOT NULL,
    faculty_id               INT         NOT NULL,
    FOREIGN KEY (specialty_requirement_id) REFERENCES uni_applications.specialties_requirements (id),
    FOREIGN KEY (specialty_program_id) REFERENCES uni_applications.specialties_programs (id),
    FOREIGN KEY (faculty_id) REFERENCES uni_applications.faculties (id)
);

CREATE TABLE uni_applications.subjects
(
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    subject_name        VARCHAR(255) NOT NULL UNIQUE,
    subject_description TEXT
);

CREATE TABLE uni_applications.specialties_subjects
(
    specialty_id INT NOT NULL,
    subject_id   INT NOT NULL,

    PRIMARY KEY (specialty_id, subject_id),
    FOREIGN KEY (specialty_id) REFERENCES uni_applications.specialties (id),
    FOREIGN KEY (subject_id) REFERENCES uni_applications.subjects (id)
);

CREATE TABLE uni_applications.teachers
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    teacher_name VARCHAR(255) NOT NULL
);

CREATE TABLE uni_applications.faculties_teachers
(
    faculty_id INT NOT NULL,
    teacher_id INT NOT NULL,

    PRIMARY KEY (faculty_id, teacher_id),
    FOREIGN KEY (faculty_id) REFERENCES uni_applications.faculties (id),
    FOREIGN KEY (teacher_id) REFERENCES uni_applications.teachers (id)
);

CREATE TABLE uni_applications.subjects_teachers
(
    teacher_id INT NOT NULL,
    subject_id INT NOT NULL,

    PRIMARY KEY (teacher_id, subject_id),
    FOREIGN KEY (teacher_id) REFERENCES uni_applications.teachers (id),
    FOREIGN KEY (subject_id) REFERENCES uni_applications.subjects (id)
);

CREATE TABLE uni_applications.students_applications
(
    username                         VARCHAR(255) NOT NULL,
    application_sent_date            DATE         NOT NULL,
    application_status               VARCHAR(30)  NOT NULL DEFAULT 'PENDING',
    application_description          TEXT         NOT NULL,
    avg_grade                        FLOAT        NOT NULL,
    language_proficiency_test_result FLOAT,
    standardized_test_result         FLOAT        NOT NULL,
    letter_of_recommendation         LONGBLOB,
    personal_statement               TEXT,
    specialty_id                     INT          NOT NULL,
    faculty_id                       INT          NOT NULL,

    PRIMARY KEY (username, specialty_id, faculty_id),
    FOREIGN KEY (specialty_id) REFERENCES uni_applications.specialties (id),
    FOREIGN KEY (faculty_id) REFERENCES uni_applications.faculties (id),
    FOREIGN KEY (application_status) REFERENCES uni_applications.application_statuses (application_status)
);

CREATE TABLE uni_applications.specialties_subjects_info
(
    specialty_id   INT   NOT NULL,
    subject_id     INT   NOT NULL,
    duration_hours FLOAT NOT NULL,

    PRIMARY KEY (specialty_id, subject_id),
    FOREIGN KEY (specialty_id) REFERENCES uni_applications.specialties (id),
    FOREIGN KEY (subject_id) REFERENCES uni_applications.subjects (id)
);

CREATE TABLE uni_applications.declined_student_applications
(
    username     VARCHAR(255) NOT NULL,
    specialty_id INT          NOT NULL,
    faculty_id   INT          NOT NULL,
    delete_date  DATE         NOT NULL,

    PRIMARY KEY (username, specialty_id, faculty_id),
    FOREIGN KEY (username, specialty_id, faculty_id) REFERENCES uni_applications.students_applications (username, specialty_id, faculty_id) ON DELETE CASCADE
);

CREATE TABLE uni_applications.students_requirements_results
(
    username                         VARCHAR(255) PRIMARY KEY NOT NULL,
    language_proficiency_test_result FLOAT,
    standardized_test_result         FLOAT
);

CREATE TABLE `uni_applications_v2`.`news`
(
    `id`              INT AUTO_INCREMENT PRIMARY KEY,
    `author_username` VARCHAR(255)  NOT NULL,
    `creation_time`   TIMESTAMP     NOT NULL,
    `news_header`     VARCHAR(1000) NOT NULL,
    `news_text`       TEXT          NOT NULL
)