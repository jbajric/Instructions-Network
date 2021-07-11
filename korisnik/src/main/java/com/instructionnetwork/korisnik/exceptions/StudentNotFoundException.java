package com.instructionnetwork.korisnik.exceptions;

public class StudentNotFoundException  extends RuntimeException {
    public StudentNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
