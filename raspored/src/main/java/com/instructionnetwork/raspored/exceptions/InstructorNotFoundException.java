package com.instructionnetwork.raspored.exceptions;

public class InstructorNotFoundException extends RuntimeException{
    public InstructorNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
