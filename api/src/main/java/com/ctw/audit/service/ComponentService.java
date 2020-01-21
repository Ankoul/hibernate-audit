package com.ctw.audit.service;

import com.ctw.audit.model.entity.Component;
import com.ctw.audit.persistence.ComponentRep;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Throwable.class)
public class ComponentService {

    private final static Logger logger = LoggerFactory.getLogger(ComponentService.class);
    private final ComponentRep componentRep;


    @Autowired
    public ComponentService(ComponentRep ComponentRep) {
        this.componentRep = ComponentRep;
    }

    public Component create(Component entity) {
        Assert.isNull(entity.getId(), "Component ID must be null on create.");
        Assert.hasText(entity.getName(), "Component must have a name.");

        logger.info("Creating Component " + entity.getName());
       return componentRep.save(entity);
    }

    public Component update(Component entity) {
        Assert.notNull(entity.getId(), "Component ID must NOT be null on update.");
        Assert.hasText(entity.getName(), "Component must have a name.");

        logger.info("Updating Component " + entity.getName());
        return componentRep.save(entity);
    }

    public void delete(String id) {
        Optional<Component> optional = componentRep.findById(id);
        Assert.isTrue(optional.isPresent(), "Component not found");

        logger.info("Deleting Component " + optional.get().getName());
        componentRep.deleteById(id);
    }

}


