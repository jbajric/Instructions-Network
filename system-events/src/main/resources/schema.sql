DROP TABLE IF EXISTS systemevents;

CREATE TABLE IF NOT EXISTS systemevents (
                                        id INT GENERATED ALWAYS AS IDENTITY,
                                        time_stamp VARCHAR(40) NOT NULL,
                                        microservice VARCHAR(40) NOT NULL,
                                        action_type VARCHAR(40) NOT NULL,
                                        resource_name VARCHAR(40) NOT NULL,
                                        response_type VARCHAR(40) NOT NULL,
                                        primary key(id)
                                        );
