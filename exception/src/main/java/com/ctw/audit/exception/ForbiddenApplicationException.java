package com.ctw.audit.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenApplicationException extends ApplicationException {
    ForbiddenApplicationException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }

}
