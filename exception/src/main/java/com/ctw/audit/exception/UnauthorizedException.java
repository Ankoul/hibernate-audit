package com.ctw.audit.exception;

import org.springframework.http.HttpStatus;

import java.io.IOException;

public class UnauthorizedException extends ApplicationException {
    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public UnauthorizedException(String message, IOException e) {
        super(HttpStatus.UNAUTHORIZED, message, e);
    }

}
