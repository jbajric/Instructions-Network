package com.instructionnetwork.korisnik.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException (String errorMessage) {
        super(errorMessage);
    }
}
