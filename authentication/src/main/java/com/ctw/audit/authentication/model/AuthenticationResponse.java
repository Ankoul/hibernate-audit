package com.ctw.audit.authentication.model;

import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess"})
public class AuthenticationResponse {
    private String token;
    private Map loggedInUser;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(Map loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
