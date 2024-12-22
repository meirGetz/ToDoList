package com.todolist.commonmodule.repositories;

import com.todolist.commonmodule.entities.Users;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository  extends JpaRepository<Users, Long> {
    List<Users> findAll();
    Users findByEmail(String email);
    Optional<Users> findByUsername(String username);
}
