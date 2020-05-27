package com.dcc.demo.service;

import com.dcc.demo.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User addUser(User user);
}
