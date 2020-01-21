package com.ctw.audit.service;

import com.ctw.audit.model.entity.UserCredentials;
import com.ctw.audit.persistence.CredentialsRep;
import com.ctw.audit.authentication.WebSecurityConfig;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CredentialsService  {

    private final CredentialsRep credentialsRep;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CredentialsService(CredentialsRep credentialsRep) {
        this.credentialsRep = credentialsRep;
        this.passwordEncoder = new BCryptPasswordEncoder(WebSecurityConfig.ENCRYPT_STRENGTH);
    }


    void create(UserCredentials credentialsEntity){
        Assert.notNull(credentialsEntity);
        Assert.hasText(credentialsEntity.getId());
        Assert.hasText(credentialsEntity.getUsername());
        Assert.hasText(credentialsEntity.getPassword());

        String password = passwordEncoder.encode(credentialsEntity.getPassword());
        credentialsEntity.setPassword(password);

        credentialsRep.save(credentialsEntity);
    }
}