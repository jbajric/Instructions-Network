package com.instructionnetwork.korisnik.model;

import lombok.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Appointment implements Serializable {

    private Integer id;
    private String date;
    private String start_time;
    private String end_time;
    private String location;
    private Integer price;
    private Integer instructor_id;
    private Integer subject_id;
}
