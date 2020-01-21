package com.ctw.audit.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApplicationException {
    BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
