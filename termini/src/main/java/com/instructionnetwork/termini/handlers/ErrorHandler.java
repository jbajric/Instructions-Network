package com.instructionnetwork.termini.handlers;

import com.instructionnetwork.termini.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = getExceptionResponse(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                                                 HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    private ApiError getExceptionResponse(String message, HttpStatus status, WebRequest request) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date(System.currentTimeMillis());
        return new ApiError(formatter.format(date),
                            status.value(),
                            message,
                            status.getReasonPhrase(),
                            ((ServletWebRequest) request).getRequest().getRequestURI()
                            );
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public final ResponseEntity<String> handleNotFoundExceptions(HttpStatusCodeException ex, WebRequest request) {
        return new ResponseEntity<String>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
    }


    @ExceptionHandler(AppointmentNotFoundException.class)
    protected ResponseEntity<Object> handleAppointmentNotFoundException(AppointmentNotFoundException ex, WebRequest request) {
        ApiError apiError = getExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoAppointmentsDefinedException.class)
    protected ResponseEntity<Object> handleNoAppointmentsDefinedException(NoAppointmentsDefinedException ex, WebRequest request) {
        ApiError apiError = getExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoAppointmentsForInstructorException.class)
    protected ResponseEntity<Object> handleNoAppointmentsForInstructorException(NoAppointmentsForInstructorException ex, WebRequest request) {
        ApiError apiError = getExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        ApiError apiError = getExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InstructorNotFoundException.class)
    protected ResponseEntity<Object> handleInstructorNotFoundException(InstructorNotFoundException ex, WebRequest request) {
        ApiError apiError = getExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    protected ResponseEntity<Object> handleStudentNotFoundException(StudentNotFoundException ex, WebRequest request) {
        ApiError apiError = getExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ApiError apiError = getExceptionResponse("Invalid format of sent data! (JSON data)", HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
