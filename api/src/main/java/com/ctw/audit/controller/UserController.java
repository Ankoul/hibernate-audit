package com.ctw.audit.controller;

import com.ctw.audit.model.entity.User;
import com.ctw.audit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@Transactional(readOnly = true)
@RequestMapping(value = "/v1/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
//    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @Transactional(rollbackFor = Throwable.class)
    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }


}
