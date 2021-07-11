package com.instructionnetwork.termini.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException (String errorMessage) {
        super(errorMessage);
    }
}
