package com.ctw.audit.authentication.model;

import org.springframework.context.ApplicationEvent;

public class AuthenticationEvent extends ApplicationEvent {

    public AuthenticationEvent(Object source) {
        super(source);
    }
}
