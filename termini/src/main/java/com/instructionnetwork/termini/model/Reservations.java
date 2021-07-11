package com.instructionnetwork.termini.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservations implements Serializable {

    private Integer id;
    private Integer appointment_id;
    private Integer student_id;

}
