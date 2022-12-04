package com.FileExplorer.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Optional;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<Object> handleException(CustomException e) {
        HttpStatus status = e.getHttpStatus() == HttpStatus.BAD_REQUEST ?
                HttpStatus.BAD_REQUEST : e.getHttpStatus();
        MyException exception = new MyException(
                e.getMessage(),
                false,
                null
        );
        return new ResponseEntity<>(exception, status);
    }
}
