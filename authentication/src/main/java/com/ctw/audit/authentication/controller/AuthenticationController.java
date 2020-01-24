package com.ctw.audit.authentication.controller;

import com.ctw.audit.authentication.service.AuthenticationService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping(value = "/v1/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ResponseBody
    @Transactional(readOnly = true)
    @RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public Object authenticate(HttpServletRequest request, HttpServletResponse response) {
        return authenticationService.authenticate(request, response);
    }

    @ResponseBody
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/validate", method = RequestMethod.GET, produces = "application/json")
    public Object validate() {
        return new Pair<>("success", true);
    }

}
