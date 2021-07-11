package com.instructionnetwork.rezervacije.exceptions;

public class NoReservationsForStudentException extends RuntimeException{
    public NoReservationsForStudentException(String errorMessage) {
        super(errorMessage);
    }
}
