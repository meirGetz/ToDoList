package com.todolist.controllers;


import com.todolist.commonmodule.entities.Users;
import com.todolist.commonmodule.repositories.UserRepository;
import com.todolist.commonmodule.security.Security.CustomUserDetailsService;
import com.todolist.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository,
                          PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @PatchMapping("/{id}/changePassword")
    public ResponseEntity<Users> changePassword(@PathVariable Long id, @RequestBody Users request) {
        Optional<Users> object = userRepository.findById(id);
        if (object.isPresent()) {
            Users user = object.get();
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/deleteUser")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
