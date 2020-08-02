package com.dcc.demo.controller;

import com.dcc.demo.model.User;
import com.dcc.demo.repository.UserRepository;
import com.dcc.demo.service.UserService;
import com.dcc.demo.util.annotation.MyUserAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @MyUserAnnotation(value = 10)
    @RequestMapping("/addUser")
    public User addUser(User user){
        return userService.addUser(user);
    }

    @MyUserAnnotation(limit = 8)
    @RequestMapping("/getAllUsers")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
}
