package com.instructionnetwork.korisnik.exceptions;

public class NoInstructorsForStudentException extends RuntimeException {
    public NoInstructorsForStudentException(String errorMessage) {super(errorMessage);}
}
