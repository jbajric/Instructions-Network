package com.instructionnetwork.korisnik.exceptions;

public class InstructorNotFoundException extends RuntimeException{
    public InstructorNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
