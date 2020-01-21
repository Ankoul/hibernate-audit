package com.ctw.audit.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Supplier;

public abstract class ApplicationException extends ResponseStatusException implements Supplier<ApplicationException> {
    ApplicationException(HttpStatus status, String message) {
        super(status, message);
    }

    ApplicationException(HttpStatus status, String message, Exception e) {
        super(status, message, e);
    }

    @Override
    public ApplicationException get() {
        return this;
    }
}
