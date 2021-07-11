package com.instructionnetwork.korisnik.exceptions;

public class SubjectNotFoundException extends RuntimeException {
    public SubjectNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
