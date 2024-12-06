package com.task.repositories;

import com.task.entities.ListObject;
import com.task.entities.Users;
import org.apache.catalina.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<Users, Long> {
    public List<Users> findAll();
    public Users findByUsername(String username);
    public Users findByEmail(String email);


}
