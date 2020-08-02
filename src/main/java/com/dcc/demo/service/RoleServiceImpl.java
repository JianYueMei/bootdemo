package com.dcc.demo.service;

import com.dcc.demo.model.Role;
import com.dcc.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Role addRole(Role role) {
        role = roleRepository.save(role);
//        int i=1/0;
        return role;
    }
}
