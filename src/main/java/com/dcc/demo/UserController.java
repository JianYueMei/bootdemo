package com.dcc.demo;

import com.dcc.demo.model.User;
import com.dcc.demo.repository.UserRepository;
import com.dcc.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/addUser")
    public User addUser(User user){
        return userService.addUser(user);
    }

    @RequestMapping("/getAllUsers")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
}
