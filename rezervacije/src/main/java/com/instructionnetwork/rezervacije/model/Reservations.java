package com.instructionnetwork.rezervacije.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservations  implements Serializable {

    @Id
    @Column (name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Digits(integer = 10, fraction = 0)
    @Range(min=1, max=2147483647)
    @NotNull
    @Column (name = "appointment_id")
    private Integer appointment_id;

    @Digits(integer = 10, fraction = 0)
    @Range(min=1, max=2147483647)
    @NotNull
    @Column (name = "student_id")
    private Integer student_id;

}
