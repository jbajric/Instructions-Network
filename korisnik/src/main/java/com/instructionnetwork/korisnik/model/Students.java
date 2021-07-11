package com.instructionnetwork.korisnik.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "students")
public class Students extends User implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "First name must be defined!")
    @Pattern(regexp = "^[A-Za-z '.-]*$",
            message = "First name should contain only letters and {''', '.', '-'}, with the first letter capitalized!")
    @Column (name = "first_name")
    private String first_name;

    @NotNull(message = "Last name must be defined!")
    @Pattern(regexp = "^[A-Za-z '.-]*$",
            message = "Last name should contain only letters and {''', '.', '-'}, with the first letter capitalized!")
    @Column (name = "last_name")
    private String last_name;

    @NotNull(message = "E-mail must be defined!")
    @Email(message = "Wrong e-mail format!")
    @Column (name = "email")
    private String email;

    @NotNull(message = "Username must be defined!")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+$",
            message = "Username should contain only letters, numbers or: {'-', '.', '_'}!")
    @Column (name = "username")
    private String username;

    @NotNull(message = "Password must be defined!")
    @Pattern(regexp = "^([A-Za-z]*[0-9]*[@#$%^&+=]*).{8,}$",
            message = "Password should be at least 8 characters long and should contain only letters, numbers or: {'@', '#', '$', '%', '^', '&', '+', '='}.")
    @Column (name = "password")
    private String password;

    @ManyToMany
    @JoinTable(
            name = "students_roles",
            joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

}
