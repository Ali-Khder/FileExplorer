package com.FileExplorer.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<Object> handleException(CustomException e) {
        MyException exception = new MyException(
                e.getMessage(),
                false,
                null
        );
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }
}
