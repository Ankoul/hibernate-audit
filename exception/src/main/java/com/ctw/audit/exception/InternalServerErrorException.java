package com.ctw.audit.exception;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends ApplicationException {
    InternalServerErrorException(String message, Exception e) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, e);
    }

}
