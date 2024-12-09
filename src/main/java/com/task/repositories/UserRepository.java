package com.task.repositories;

import com.task.entities.Users;
import org.apache.catalina.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository  extends JpaRepository<Users, Long> {
    public List<Users> findAll();
    public Users findByEmail(String email);
    Optional<Users> findByUsername(String username);




}
