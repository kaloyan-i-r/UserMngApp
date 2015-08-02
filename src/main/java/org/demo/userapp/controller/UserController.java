package org.demo.userapp.controller;

import org.demo.userapp.model.entities.User;
import org.demo.userapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by k on 7/30/2015.
 */
@RestController
public class UserController {

    @Autowired
    private UserService userSrv;

    @RequestMapping("/rest/users/list")
    public List<User> list()
    {
        return userSrv.findAll();
    }

    @ResponseBody
    @RequestMapping("/rest/users/create")
    public User create(@RequestBody  @Valid User user)
    {
        return userSrv.create(user);
    }

    @ResponseBody
    @RequestMapping("/rest/users/update")
    public User update(@RequestBody  @Valid User user)
    {
        return userSrv.update(user);
    }

    @RequestMapping(value = "/rest/users/delete/{userId}")
    public void delete(@PathVariable("userId")Long id)
    {
        userSrv.delete(id);
    }

}
