package com.instructionnetwork.raspored.exceptions;

public class NoFreeTimesForStudentException extends RuntimeException {

    public NoFreeTimesForStudentException(String s) {
        super(s);
    }
}
