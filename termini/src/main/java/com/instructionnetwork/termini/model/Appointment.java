package com.instructionnetwork.termini.model;

import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment implements Serializable {

    @Id
    @Column (name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](20|21)\\d\\d$",
            message = "Allowed date formats:  {dd/mm/yyyy, dd-mm-yyyy, dd.mm.yyyy}!")
    @Column (name = "date_")
    private String date;

    @NotNull
    @Pattern(regexp = "^(([01]\\d|2[0-3]):([0-5]\\d)|24:00)$",
            message = "Allowed time formats: {HH:MM}")
    @Column (name = "start_time")
    private String start_time;

    @NotNull
    @Pattern(regexp = "^(([01]\\d|2[0-3]):([0-5]\\d)|24:00)$",
            message = "Allowed time formats: {HH:MM}")
    @Column (name = "end_time")
    private String end_time;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z_ ]*$",
            message = "Location name should contain only letters!")
    @Column (name = "location")
    private String location;

    @NotNull
    @Digits(integer = 10, fraction = 0)
    @Range(min=1, max=100)
    @Column (name = "price")
    private Integer price;

    private Boolean available;

    @NotNull
    @Column (name = "instructor_id")
    private Integer instructor_id;

    @NotNull
    @Column (name = "subject_id")
    private Integer subject_id;
}
