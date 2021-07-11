package com.instructionnetwork.korisnik.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private Integer id;
    private String first_name;
    private String last_name;
    private String email;
    private String username;
    private String password;


}
