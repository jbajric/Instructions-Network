package com.instructionnetwork.termini.exceptions;

public class InstructorNotFoundException extends RuntimeException{
    public InstructorNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
