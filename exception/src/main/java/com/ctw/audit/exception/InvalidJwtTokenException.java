package com.ctw.audit.exception;

import java.io.IOException;

public class InvalidJwtTokenException extends UnauthorizedException {
    public InvalidJwtTokenException(String message, IOException e) {
        super(message, e);
    }
}
