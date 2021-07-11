package com.instructionnetwork.rezervacije.exceptions;

public class ReservationNotFoundException extends RuntimeException{
    public ReservationNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
