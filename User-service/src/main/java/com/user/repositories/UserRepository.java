package com.user.repositories;

import com.user.entities.Users;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
@Scope("singleton")
@Lazy
public interface UserRepository extends JpaRepository<Users, Long> {
    List<Users> findAll();

    Users findByEmail(String email);

    Optional<Users> findByUsername(String username);


}
