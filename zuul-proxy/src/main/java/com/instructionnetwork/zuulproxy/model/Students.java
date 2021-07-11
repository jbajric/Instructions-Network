package com.instructionnetwork.zuulproxy.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Students extends User implements Serializable {

    private Integer id;
    private String first_name;
    private String last_name;
    private String email;
    private String username;
    private String password;
    private Set<Role> roles = new HashSet<>();

}
