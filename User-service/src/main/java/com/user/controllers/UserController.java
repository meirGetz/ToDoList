package com.user.controllers;

import com.user.DTO.UserDto;
import com.user.DTO.LoginRequest;
import com.user.auth.Security.CustomUserDetailsService;
import com.user.auth.Security.JwtUtil;
import com.user.Service.UserService;
import com.user.entities.Users;
import com.user.repositories.UserRepository;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.Collections;
import java.util.List;
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
                          PasswordEncoder passwordEncoder,CustomUserDetailsService userDetailsService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

@PostMapping("/registerNewUserAccount")
public ResponseEntity<Users> createUser(@RequestBody Users user) {
    try {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        Users save = userRepository.save(user);
        return ResponseEntity.ok(user);
    } catch (Exception e) {
        // Log the error
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Users user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        // יצירת אובייקט User מ-Spring Security
        org.springframework.security.core.userdetails.User springUser =
                new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);

        // יצירת authenticationToken
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(springUser, null, authorities);

        // עדכון ה-SecurityContext עם המשתמש שנכנס
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // יצירת הטוקן
        String token = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(Collections.singletonMap("token", token));
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