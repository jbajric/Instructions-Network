DROP TABLE IF EXISTS appointments;

CREATE TABLE IF NOT EXISTS appointments (
                                        id INT GENERATED ALWAYS AS IDENTITY,
                                        date_ VARCHAR(20) NOT NULL,
                                        start_time VARCHAR(20) NOT NULL,
                                        end_time VARCHAR(20) NOT NULL,
                                        location VARCHAR(20) NOT NULL,
                                        price INTEGER NOT NULL,
                                        available BOOLEAN,
                                        instructor_id INTEGER NOT NULL,
                                        subject_id INTEGER NOT NULL,
                                        primary key(id)
                                        );