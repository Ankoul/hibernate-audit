package com.ctw.audit.authentication.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;


@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticatedUser implements Authentication, ResponseData<Map> {

    private static final ThreadLocal<Authentication> threadLocalAuthentication = new ThreadLocal<>();
    private boolean authenticated;
    private Map userMap;
    private String token;

    public AuthenticatedUser() {
        this.authenticated = true;
    }

    @SuppressWarnings("unchecked")
    public AuthenticatedUser(Map userMap) {
        this.authenticated = true;
        this.userMap = userMap;
        if(userMap != null){
            this.token = (String) userMap.getOrDefault("jwt", null);
        }
    }

    public AuthenticatedUser(AuthenticationResponse authResponse) {
        this.authenticated = true;
        this.userMap = authResponse.getLoggedInUser();
        this.token = authResponse.getToken();
    }

    private AuthenticatedUser(boolean isAuthenticated) {
        this.authenticated = isAuthenticated;
    }

    public static AuthenticatedUser instanceNotAuthenticatedUser(){
        return new AuthenticatedUser(false);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public static String getAuthToken() {
        final AuthenticatedUser authentication = (AuthenticatedUser) getAuthentication();
        if(authentication != null){
            return authentication.token;
        }
        return null;
    }

    public static String getId(){
        final Map authentication = getAuthenticationMap();
        if(authentication == null){
            return null;
        }
        return authentication.get("id").toString();
    }
    public static String getCompanyId(){
        final Map authentication = getAuthenticationMap();
        if(authentication == null){
            return null;
        }
        return authentication.get("companyId").toString();
    }

    public static String getCompanyName(){
        final Map authentication = getAuthenticationMap();
        if(authentication == null){
            return null;
        }
        return authentication.get("companyName").toString();
    }

    public static String getDisplayName(){
        final Map authentication = getAuthenticationMap();
        if(authentication == null){
            return null;
        }
        return authentication.get("displayName").toString();
    }
    public static String getTimezone(){
        final Map authentication = getAuthenticationMap();
        if(authentication == null){
            return null;
        }
        return authentication.get("timezone").toString();
    }

    private static Map getAuthenticationMap(){
        final Authentication authentication = getAuthentication();
        if(authentication == null){
            return null;
        }
        return (Map) authentication.getPrincipal();
    }

    private static Authentication getAuthentication() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            return authentication;
        }
        return threadLocalAuthentication.get();
    }

    public static void setThreadLocalAuthentication(Authentication authentication){
        threadLocalAuthentication.set(authentication);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(userMap == null){
            return Collections.emptyList();
        }
        String ROLE_PREFIX = "ROLE_";
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<String> roles = (List<String>) this.userMap.get("roles");
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role.toUpperCase()));
        }
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return this.userMap;
    }

    @Override
    public Object getDetails() {
        return this.userMap;
    }

    @Override
    public Object getPrincipal() {
        return this.userMap;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return this.userMap != null && this.userMap.get("displayName") != null ? this.userMap.get("displayName").toString() : "";
    }

    @Override
    public Map getData() {
        return userMap;
    }

    @Override
    public void setData(Map data) {
        this.userMap = data;
    }
}
