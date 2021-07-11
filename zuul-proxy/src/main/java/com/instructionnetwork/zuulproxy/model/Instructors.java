package com.instructionnetwork.zuulproxy.model;

import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class Instructors extends User implements Serializable {

    private Integer id;
    private String first_name;
    private String last_name;
    private String email;
    private String username;
    private String password;
    private List<InstructorsRatings> instructorsRatings;
    private Set<Role> roles = new HashSet<>();

}
