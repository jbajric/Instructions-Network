package com.instructionnetwork.rezervacije.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Instructors extends User implements Serializable {

    private Integer id;
    private String first_name;
    private String last_name;
    private String email;
    private String username;
    private String password;
    private Set<Role> roles = new HashSet<>();

}
