package com.task.controllers;

import com.task.DTO.UserDto;
import com.task.Service.UserService;
import com.task.entities.Users;
import org.apache.catalina.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.*;
import com.task.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserService userService, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registerNewUserAccount")
    public ResponseEntity<Users> createUser(@RequestBody UserDto userDto, WebRequest request) {
        Optional<Users> object;
        Users user = new Users();
        user.setUsername(userDto.getFirstName() + " " + userDto.getLastName());
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encryptedPassword);
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setRoles(Collections.singletonList("USER"));
        userRepository.save(user);
//        userService.registerNewUserAccount(userDto);
        return ResponseEntity.ok(user);
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
