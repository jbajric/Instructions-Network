package com.instructionnetwork.termini.exceptions;

public class StudentNotFoundException  extends RuntimeException {
    public StudentNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
