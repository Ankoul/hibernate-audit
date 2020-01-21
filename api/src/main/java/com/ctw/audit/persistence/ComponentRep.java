package com.ctw.audit.persistence;

import com.ctw.audit.model.entity.Application;
import com.ctw.audit.model.entity.Component;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ComponentRep extends CrudRepository<Component, String> {

}
