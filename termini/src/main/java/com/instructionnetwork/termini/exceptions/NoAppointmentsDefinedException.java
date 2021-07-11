package com.instructionnetwork.termini.exceptions;

public class NoAppointmentsDefinedException extends RuntimeException {
    public NoAppointmentsDefinedException(String errorMessage) {
        super(errorMessage);
    }
}