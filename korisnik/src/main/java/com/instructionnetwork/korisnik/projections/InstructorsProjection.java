package com.instructionnetwork.korisnik.projections;

import org.springframework.beans.factory.annotation.Value;

public interface InstructorsProjection {

    Integer getId();
    @Value("#{target.first_name + ' ' + target.last_name}")
    String getName();
    String getEmail();
    String getUsername();
    Float getAverageRating();
}
