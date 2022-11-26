package com.FileExplorer.handler;

import org.springframework.http.HttpStatus;

public class MyException {
    private final String message;
    private final boolean status;
    private final String data;

    public MyException(String message, boolean status, String data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public String getData() {
        return data;
    }
}
