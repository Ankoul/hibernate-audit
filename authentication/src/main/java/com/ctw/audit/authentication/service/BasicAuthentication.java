package com.ctw.audit.authentication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.security.core.Authentication;
import org.springframework.util.Base64Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

abstract class BasicAuthentication {

    private final static Logger logger = LoggerFactory.getLogger(BasicAuthentication.class);

    Map authenticate(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Authorization");
        Authentication authUser = authenticate(token);

        response.addHeader("Authorization", "Bearer " + authUser.getCredentials().toString());
        response.addHeader("access-control-expose-headers", "Authorization");

        logger.info("user " + authUser.getName() + " has login");

        Object details = authUser.getDetails() != null ? authUser.getDetails() : authUser.getPrincipal();
        if(details instanceof Map){
            return (Map) details;
        }
        return new ObjectMapper().convertValue(details, Map.class);
    }

    private Authentication authenticate(String token) {
        token = token.replace("Basic ", "");

        UserCredentials credentials = parseUserCredentials(token);
        return authenticate(credentials);
    }

    private UserCredentials parseUserCredentials(String token) {
        byte[] decoder = Base64Utils.decodeFromString(token);
        String decoded = new String(decoder, StandardCharsets.UTF_8);

        int i = decoded.indexOf(':');
        String username = decoded.substring(0,i);
        String password = decoded.substring(i+1);
        return new UserCredentials(username, password);
    }
    protected abstract Authentication authenticate(UserCredentials credentials);
}
