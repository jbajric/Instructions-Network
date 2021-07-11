package com.instructionnetwork.rezervacije.exceptions;

public class InstructorNotFoundException extends RuntimeException{
    public InstructorNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
