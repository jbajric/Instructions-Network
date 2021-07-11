package com.instructionnetwork.korisnik.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "instructors_ratings")
public class InstructorsRatings implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Rating must be defined!")
    @Digits(fraction = 0, integer = 1, message = "Rating must be in range 1-5!")
    @Min(value = 1, message = "Rating must be in range 1-5!")
    @Max(value = 5, message = "Rating must be in range 1-5!")
    @Column(name = "rating")
    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "instructor_id", referencedColumnName = "id")
    private Instructors instructors;

    @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Students students;

}
