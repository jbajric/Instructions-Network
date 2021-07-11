package com.instructionnetwork.raspored.exceptions;

public class StudentNotFoundException  extends RuntimeException {
    public StudentNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
