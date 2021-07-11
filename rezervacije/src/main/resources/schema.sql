
DROP TABLE IF EXISTS reservations;

CREATE TABLE IF NOT EXISTS reservations (
                                            id INT GENERATED ALWAYS AS IDENTITY,
                                            appointment_id INT NOT NULL,
                                            student_id INT NOT NULL,
                                            primary key(id)
                                        );
