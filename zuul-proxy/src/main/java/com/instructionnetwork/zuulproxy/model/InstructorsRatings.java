package com.instructionnetwork.zuulproxy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorsRatings implements Serializable {

    private Integer id;
    private Integer rating;
    private Instructors instructors;
    private Students students;

}
