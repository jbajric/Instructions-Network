package com.instructionnetwork.zuulproxy.exceptions;

public class InstructorNotFoundException extends RuntimeException{
    public InstructorNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
