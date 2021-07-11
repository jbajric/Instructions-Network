package com.instructionnetwork.korisnik.exceptions;

public class NoSubjectsDefinedException extends RuntimeException {
    public NoSubjectsDefinedException(String errorMessage) {
        super(errorMessage);
    }
}
