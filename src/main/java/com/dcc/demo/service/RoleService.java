package com.dcc.demo.service;

import com.dcc.demo.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role addRole(Role role);
}
