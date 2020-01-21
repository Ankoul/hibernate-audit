package com.ctw.audit.controller;

import com.ctw.audit.model.entity.Application;
import com.ctw.audit.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/v1/applications")
public class ApplicationController {

    private ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
    public Application create(@RequestBody Application application) {
        return applicationService.create(application);
    }

    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json")
    public Application update(@PathVariable String id, @RequestBody Application application)  {
        application.setId(id);
        return applicationService.update(application);
    }

    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String id)  {
        applicationService.delete(id);
    }

}