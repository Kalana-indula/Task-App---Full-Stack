package com.todo.backend.exception;

import com.todo.backend.exception.error.TaskErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TaskExceptionHandler {

    // handle not found exceptions
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<TaskErrorResponse> handleTaskNotFound(TaskNotFoundException exc) {

        TaskErrorResponse error = new TaskErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // generic handler for any unhandled exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<TaskErrorResponse> handleGenericException(Exception exc) {

        TaskErrorResponse error = new TaskErrorResponse();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage("Unexpected error: " + exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //handle empty data inputs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<TaskErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        TaskErrorResponse error = new TaskErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(errorMessage);
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
