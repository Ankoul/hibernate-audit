package com.ctw.audit.controller;

import com.ctw.audit.model.entity.Component;
import com.ctw.audit.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/v1/components")
public class ComponentController {

    private ComponentService componentService;

    @Autowired
    public ComponentController(ComponentService ComponentService) {
        this.componentService = ComponentService;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @RequestMapping(value = "", method = RequestMethod.POST, produces = "Component/json")
    public Component create(@RequestBody Component Component) {
        return componentService.create(Component);
    }

    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "Component/json")
    public Component update(@PathVariable String id, @RequestBody Component Component)  {
        Component.setId(id);
        return componentService.update(Component);
    }

    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String id)  {
        componentService.delete(id);
    }

}