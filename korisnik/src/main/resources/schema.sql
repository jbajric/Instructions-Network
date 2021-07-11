DROP TABLE IF EXISTS students_subjects;
DROP TABLE IF EXISTS instructors_subjects;
DROP TABLE IF EXISTS instructors_ratings;
DROP TABLE IF EXISTS students_roles;
DROP TABLE IF EXISTS instructors_roles;
DROP TABLE IF EXISTS instructors;
DROP TABLE IF EXISTS subjects;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS roles;


CREATE TABLE IF NOT EXISTS instructors (
                                        id INT GENERATED ALWAYS AS IDENTITY,
                                        first_name VARCHAR(20) NOT NULL,
                                        last_name VARCHAR(30) NOT NULL,
                                        email VARCHAR(40) NOT NULL,
                                        username VARCHAR(20) NOT NULL UNIQUE,
                                        password VARCHAR(255) NOT NULL,
                                        primary key(id)
                                        );

CREATE TABLE IF NOT EXISTS students (
                                    id INT GENERATED ALWAYS AS IDENTITY,
                                    first_name VARCHAR(20) NOT NULL,
                                    last_name VARCHAR(30) NOT NULL,
                                    email VARCHAR(40) NOT NULL,
                                    username VARCHAR(20) NOT NULL UNIQUE,
                                    password VARCHAR(255) NOT NULL,
                                    primary key(id)
                                    );

CREATE TABLE IF NOT EXISTS subjects (
                                    id INT GENERATED ALWAYS AS IDENTITY,
                                    subject_name VARCHAR(255) NOT NULL,
                                    primary key(id)
                                    );

CREATE TABLE IF NOT EXISTS instructors_subjects (
                                                instructor_id INTEGER NOT NULL,
                                                subject_id INTEGER NOT NULL,
                                                PRIMARY KEY (instructor_id, subject_id),
                                                FOREIGN KEY (instructor_id) REFERENCES Instructors (id),
                                                FOREIGN KEY (subject_id) REFERENCES Subjects (id)
                                                );

CREATE TABLE IF NOT EXISTS students_subjects (
                                            student_id INTEGER NOT NULL,
                                            subject_id INTEGER NOT NULL,
                                            PRIMARY KEY (student_id, subject_id),
                                            FOREIGN KEY (student_id) REFERENCES Students (id),
                                            FOREIGN KEY (subject_id) REFERENCES Subjects (id)
                                            );

CREATE TABLE IF NOT EXISTS instructors_ratings (
                                                id INT GENERATED ALWAYS AS IDENTITY,
                                                rating INTEGER NOT NULL,
                                                instructor_id INTEGER NOT NULL,
                                                student_id INTEGER NOT NULL,
                                                PRIMARY KEY (id),
                                                FOREIGN KEY (instructor_id) REFERENCES Instructors (id),
                                                FOREIGN KEY (student_id) REFERENCES students (id)
                                                );

CREATE TABLE IF NOT EXISTS roles (
                                   id BIGINT GENERATED ALWAYS AS IDENTITY,
                                   name VARCHAR(60) NOT NULL,
                                   PRIMARY KEY (id)
                                   );

CREATE TABLE IF NOT EXISTS students_roles (
                                  student_id INTEGER NOT NULL,
                                  role_id BIGINT NOT NULL,
                                  PRIMARY KEY (student_id, role_id),
                                  FOREIGN KEY (student_id) REFERENCES Students (id),
                                  FOREIGN KEY (role_id) REFERENCES Roles (id)
                                  );

CREATE TABLE IF NOT EXISTS instructors_roles (
                                  instructor_id INTEGER NOT NULL,
                                  role_id BIGINT NOT NULL,
                                  PRIMARY KEY (instructor_id, role_id),
                                  FOREIGN KEY (instructor_id) REFERENCES Instructors (id),
                                  FOREIGN KEY (role_id) REFERENCES Roles (id)
                                  );