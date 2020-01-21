package com.ctw.audit.service;

import com.ctw.audit.exception.UserNotFoundException;
import com.ctw.audit.model.bean.UserDetailsImpl;
import com.ctw.audit.model.entity.UserCredentials;
import com.ctw.audit.model.entity.User;
import com.ctw.audit.persistence.CredentialsRep;
import com.ctw.audit.persistence.UserRep;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    private final CredentialsRep credentialsRep;
    private UserRep userRep;

    public UserDetailsServiceImpl(CredentialsRep credentialsRep, UserRep userRep) {
        this.credentialsRep = credentialsRep;
        this.userRep = userRep;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserCredentials user = credentialsRep.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(user);
    }
}