package com.instructionnetwork.rezervacije.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException (String errorMessage) {
        super(errorMessage);
    }
}
