package com.dcc.demo.service;

import com.dcc.demo.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    List<Role> getAllRolesFromHash();
    Role getRoleById(Long id);
    Role addRole(Role role);
    Integer syncAllRoles();
}
