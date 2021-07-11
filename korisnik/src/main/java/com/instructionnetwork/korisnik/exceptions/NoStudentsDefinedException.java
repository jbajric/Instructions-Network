package com.instructionnetwork.korisnik.exceptions;

public class NoStudentsDefinedException extends RuntimeException {
    public NoStudentsDefinedException(String errorMessage) {
        super(errorMessage);
    }
}
