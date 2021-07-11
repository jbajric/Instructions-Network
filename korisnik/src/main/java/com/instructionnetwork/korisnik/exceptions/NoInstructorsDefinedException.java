package com.instructionnetwork.korisnik.exceptions;

public class NoInstructorsDefinedException extends RuntimeException {
    public NoInstructorsDefinedException(String errorMessage) {
        super(errorMessage);
    }
}