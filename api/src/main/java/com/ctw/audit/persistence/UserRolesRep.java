package com.ctw.audit.persistence;

import com.ctw.audit.model.entity.UserRoles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRolesRep extends CrudRepository<UserRoles, String> {

}
