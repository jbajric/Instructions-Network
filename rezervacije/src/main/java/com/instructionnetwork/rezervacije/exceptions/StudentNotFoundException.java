package com.instructionnetwork.rezervacije.exceptions;

public class StudentNotFoundException  extends RuntimeException {
    public StudentNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
