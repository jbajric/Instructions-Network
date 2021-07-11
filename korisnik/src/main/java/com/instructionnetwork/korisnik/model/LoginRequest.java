package com.instructionnetwork.korisnik.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequest {

    @NotNull(message = "Username must be defined!")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+$",
            message = "Username should contain only letters, numbers or: {'-', '.', '_'}!")
    @Column(name = "username")
    private String username;

    @NotNull(message = "Password must be defined!")
    @Pattern(regexp = "^([A-Za-z]*[0-9]*[@#$%^&+=]*).{8,}$",
            message = "Password should be at least 8 characters long and should contain only letters, numbers or: {'@', '#', '$', '%', '^', '&', '+', '='}.")
    @Column (name = "password")
    private String password;
}
