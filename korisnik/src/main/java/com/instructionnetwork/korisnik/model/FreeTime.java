package com.instructionnetwork.korisnik.model;

import lombok.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FreeTime implements Serializable {

    private Integer id;
    private String date;
    private String start_time;
    private String end_time;
    private Integer student_id;
}
