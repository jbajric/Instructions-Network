package com.instructionnetwork.termini.exceptions;

public class NoReservationsDefinedException extends RuntimeException{
    public NoReservationsDefinedException(String errorMessage) {
        super(errorMessage);
    }
}
