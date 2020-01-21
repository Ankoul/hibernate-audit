package com.ctw.audit.service;

import com.ctw.audit.model.entity.User;
import com.ctw.audit.model.entity.UserCredentials;
import com.ctw.audit.model.entity.UserRoles;
import com.ctw.audit.model.enumaration.RolesEnum;
import com.ctw.audit.persistence.UserRep;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.ctw.audit.model.enumaration.RolesEnum.*;
import static org.springframework.util.CollectionUtils.isEmpty;

@SuppressWarnings({"WeakerAccess", "SpringJavaAutowiredFieldsWarningInspection"})
@Service
public class UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRep userRep;
    private CredentialsService credentialsService;
    private UserRolesService userRolesService;


    @Autowired
    public UserService(UserRep userRep, CredentialsService credentialsService, UserRolesService userRolesService) {
        this.userRep = userRep;
        this.credentialsService = credentialsService;
        this.userRolesService = userRolesService;
    }

    @SuppressWarnings("unchecked")
    public User create(User entity) {
        Assert.notNull(entity, "body cannot be empty");
        Assert.isNull(entity.getId(), "you cannot create an user with ID");
        Assert.notNull(entity.getUsername(), "you must define an username");

        Assert.isTrue(!userRep.existsByUsername(entity.getUsername()), "username already exists");

        List<UserRoles> roles = entity.getRoles();
        if(!isEmpty(entity.getRoles())){
            roles = entity.getRoles().stream()
                    .filter(role -> role.getRole() != NONE)
                    .collect(Collectors.toList());
        }
        entity.setRoles(null);
        entity = userRep.save(entity);
        if (isEmpty(roles)) {
            return entity;
        }
        List<UserRoles> userRoles = userRolesService.create(roles, entity.getId());
        entity.setRoles(userRoles);

        UserCredentials credentialsEntity = new UserCredentials(entity, "123456");
        this.credentialsService.create(credentialsEntity);
        return entity;
    }

}


