package com.dcc.demo.controller;

import com.dcc.demo.model.Role;
import com.dcc.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("/addRole")
    public Role addUser(Role role){
        return roleService.addRole(role);
    }

    @RequestMapping("/getAllRoles")
    public List<Role> getAllUsers(){
        return roleService.getAllRoles();
    }
}
