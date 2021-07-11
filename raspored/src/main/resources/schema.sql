DROP TABLE IF EXISTS freetimes;

CREATE TABLE IF NOT EXISTS freetimes (
                                        id INT GENERATED ALWAYS AS IDENTITY,
                                        date_ VARCHAR(20) NOT NULL,
                                        start_time VARCHAR(20) NOT NULL,
                                        end_time VARCHAR(20) NOT NULL,
                                        student_id INT NOT NULL,
                                        primary key(id)
                                        );