package org.demo.userapp.controller;

import org.demo.userapp.model.entities.User;
import org.demo.userapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by k on 7/30/2015.
 */
@RestController
public class UserController {

    @Autowired
    private UserService userSrv;

    @RequestMapping("/users/list")
    public List<User> list()
    {
        return userSrv.findAll();
    }

}
