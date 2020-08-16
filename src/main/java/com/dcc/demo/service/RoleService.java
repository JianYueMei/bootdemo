package com.dcc.demo.service;

import com.dcc.demo.model.Role;
import com.dcc.demo.vo.RoleVo;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    List<Role> getAllRolesFromHash();
    Role addRole(Role role);
    Integer syncAllRoles();

    RoleVo getRoleById(Long id);
    List<RoleVo> getRoleByIds(List<Long> ids);

}
