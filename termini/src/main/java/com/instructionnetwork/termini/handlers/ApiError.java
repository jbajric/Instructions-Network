package com.instructionnetwork.termini.handlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private String timestamp;
    private int status;
    private String message;
    private String error;
    private String path;
}