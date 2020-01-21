package com.ctw.audit.service;

import com.ctw.audit.model.entity.UserRoles;
import com.ctw.audit.persistence.UserRolesRep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@SuppressWarnings({"WeakerAccess", "SpringJavaAutowiredFieldsWarningInspection"})
@Service
public class UserRolesService {

    private final UserRolesRep userRolesRep;


    @Autowired
    public UserRolesService(UserRolesRep userRolesRep) {
        this.userRolesRep = userRolesRep;
    }

    public List<UserRoles> create(List<UserRoles> roles, String userId) {
        if(CollectionUtils.isEmpty(roles)){
            return roles;
        }
        for (UserRoles role : roles) {
            role.setUserId(userId);
        }
        userRolesRep.saveAll(roles);
        return roles;
    }

}


