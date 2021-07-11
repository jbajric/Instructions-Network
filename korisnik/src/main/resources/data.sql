INSERT INTO instructors (first_name, last_name, email, username, password)
    VALUES ('Irfan', 'Prazina', 'ip@gmail.com', 'iprazina', '$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG'),
           ('Emir', 'Cogo', 'ec@gmail.com', 'ecogo', '$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG');

INSERT INTO students (first_name, last_name, email, username, password )
    VALUES ('Nejira', 'Music', 'nm@gmail.com', 'nmusic', '$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG'),
           ('Jasmin', 'Bajric', 'jb@gmail.com', 'jbajric', '$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG'),
           ('Elma', 'Bejtovic', 'eb@gmail.com', 'ebejtovic', '$2a$10$IkYWO5mUX97ei/K2f7Hm8uZBm7l35817yJeS1xyttoKE3bNHj4ucG');

INSERT INTO subjects (subject_name)
    VALUES ('Calculus'), ('Algorithms'), ('Computer Graphics');

INSERT INTO instructors_subjects (instructor_id, subject_id)
    VALUES (1, 1);

INSERT INTO students_subjects (student_id, subject_id)
    VALUES (1, 1);

INSERT INTO instructors_ratings(rating, instructor_id, student_id)
    VALUES (5, 1, 1), (1, 1, 1);

INSERT INTO roles(name)
    VALUES ('INSTRUCTOR'), ('STUDENT');

INSERT INTO students_roles (student_id, role_id)
    VALUES (1, 2), (2, 2), (3, 2);

INSERT INTO instructors_roles (instructor_id, role_id)
    VALUES (1, 1), (2, 1);


