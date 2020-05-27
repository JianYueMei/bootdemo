package com.dcc.demo.repository;

import com.dcc.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  UserRepository  extends JpaRepository<User,Long> {
    User findByName(String userName);
}
