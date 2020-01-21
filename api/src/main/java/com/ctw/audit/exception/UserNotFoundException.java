package com.ctw.audit.exception;

public class UserNotFoundException extends BadRequestException{
    public UserNotFoundException(String id) {
        super("user " + id + " not found");
    }
}
