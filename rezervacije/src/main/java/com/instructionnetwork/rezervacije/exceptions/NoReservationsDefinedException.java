package com.instructionnetwork.rezervacije.exceptions;

public class NoReservationsDefinedException extends RuntimeException{
    public NoReservationsDefinedException(String errorMessage) {
        super(errorMessage);
    }
}
