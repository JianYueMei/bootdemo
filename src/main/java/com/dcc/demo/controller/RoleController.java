package com.dcc.demo.controller;

import com.dcc.demo.model.Role;
import com.dcc.demo.service.RoleService;
import com.dcc.demo.service.RoleServiceImpl;
import com.dcc.demo.vo.RoleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController("/role")
public class RoleController {
    private final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @PostMapping("/addRole")
    public Role addUser(@RequestBody Role role){
        logger.info("新增角色信息:{}",role.toString());
        return roleService.addRole(role);
    }

    @GetMapping("/getAllRoles")
    public List<Role> getAllUsers(){
        return roleService.getAllRoles();
    }

    @GetMapping("/getAllRolesFromhash")
    public List<Role> getAllRolesFromhash(){
        return roleService.getAllRolesFromHash();
    }

    @GetMapping("/getRoleById")
    public RoleVo getRoleById(@RequestParam Long id){
        return roleService.getRoleById(id);
    }

    @GetMapping("/getRoleByIds")
    public List<RoleVo> getRoleByIds(@RequestParam List<Long> ids){
        return roleService.getRoleByIds(ids);
    }
}
