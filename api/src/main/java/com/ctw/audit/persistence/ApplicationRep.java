package com.ctw.audit.persistence;

import com.ctw.audit.model.entity.Application;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ApplicationRep extends CrudRepository<Application, String> {

}
