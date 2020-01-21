package com.ctw.audit.service;

import com.ctw.audit.model.entity.Application;
import com.ctw.audit.persistence.ApplicationRep;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Throwable.class)
public class ApplicationService {

    private final static Logger logger = LoggerFactory.getLogger(ApplicationService.class);
    private final ApplicationRep applicationRep;


    @Autowired
    public ApplicationService(ApplicationRep applicationRep) {
        this.applicationRep = applicationRep;
    }

    public Application create(Application entity) {
        Assert.isNull(entity.getId(), "Application ID must be null on create.");
        Assert.hasText(entity.getName(), "Application must have a name.");

        logger.info("Creating Application " + entity.getName());
       return applicationRep.save(entity);
    }

    public Application update(Application entity) {
        Assert.notNull(entity.getId(), "Application ID must NOT be null on update.");
        Assert.hasText(entity.getName(), "Application must have a name.");

        logger.info("Updating Application " + entity.getName());
        return applicationRep.save(entity);
    }

    public void delete(String id) {
        Optional<Application> optional = applicationRep.findById(id);
        Assert.isTrue(optional.isPresent(), "Application not found");

        logger.info("Deleting Application " + optional.get().getName());
        applicationRep.deleteById(id);
    }

}


