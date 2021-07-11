package com.instructionnetwork.termini.exceptions;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}