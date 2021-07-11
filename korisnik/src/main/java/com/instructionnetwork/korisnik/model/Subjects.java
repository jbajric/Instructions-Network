package com.instructionnetwork.korisnik.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subjects")
public class Subjects implements Serializable {

    @NotNull
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Subject name must be defined!")
    @Pattern(regexp = "^[a-zA-Z_ ]*$", message = "Subject name should contain only letters!")
    @Column (name = "subject_name")
    private String subject_name;

    @ManyToMany
    @JoinTable(
            name = "students_subjects",
            joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id")
    )
    private List<Students> students;

    @ManyToMany
    @JoinTable(
            name = "instructors_subjects",
            joinColumns = @JoinColumn(name = "instructor_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id")
    )
    private List<Instructors> instructors;

}
