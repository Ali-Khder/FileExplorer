package com.FileExplorer.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ValidationHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handle(BindException e) {
        Map<String, Object> res = new HashMap<>();
        res.put("status", false);
        res.put("message",
                e.getBindingResult().getFieldErrors().get(0).getField() + " " +
                        e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
        res.put("data", null);

        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }
}
