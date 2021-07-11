package com.instructionnetwork.raspored.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException (String errorMessage) {
        super(errorMessage);
    }
}
