package com.dcc.demo.repository;

import com.dcc.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String roleName);
}
