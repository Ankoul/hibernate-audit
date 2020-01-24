package com.ctw.audit.authentication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@SuppressWarnings({"SpringJavaAutowiringInspection", "SpringJavaAutowiredFieldsWarningInspection"})
public class AuthenticationService {

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private JwtAuthenticationService authenticationService;

    public Object authenticate(HttpServletRequest request, HttpServletResponse response) {
        return authenticationService.authenticate(request, response);
    }

    public Authentication authenticate(HttpServletRequest request) {
        return authenticationService.authenticate(request);
    }
}
