package com.ctw.audit.authentication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.security.core.Authentication;
import org.springframework.util.Base64Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

abstract class BasicAuthentication {

    private final static Logger logger = LoggerFactory.getLogger(BasicAuthentication.class);
    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String BEARER_PREFIX = "Bearer ";
    protected static final String BASIC_PREFIX = "Basic ";

    Object authenticate(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        Authentication authUser = authenticate(token);

        response.addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + authUser.getCredentials().toString());
        response.addHeader("access-control-expose-headers", "Authorization");

        logger.info("user " + authUser.getName() + " has login");

        return authUser.getDetails() != null ? authUser.getDetails() : authUser.getPrincipal();
    }

    private Authentication authenticate(String token) {
        token = token.replace(BASIC_PREFIX, "");

        UserCredentials credentials = parseUserCredentials(token);
        return authenticate(credentials);
    }

    private UserCredentials parseUserCredentials(String token) {
        byte[] decoder = Base64Utils.decodeFromString(token);
        String decoded = new String(decoder, StandardCharsets.UTF_8);

        int i = decoded.indexOf(':');
        String username = decoded.substring(0, i);
        String password = decoded.substring(i + 1);
        return new UserCredentials(username, password);
    }

    protected abstract Authentication authenticate(UserCredentials credentials);
}
