package com.user.controllers;

import com.DTO.LoginRequest;
import com.DTO.UserDto;
import com.user.auth.Security.JwtUtil;
import com.user.entities.Users;
import com.user.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    @Autowired
    public UserController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/registerNewUserAccount")
    public ResponseEntity<Users> createUser( @RequestBody UserDto userDto) {
        System.out.println("in @PostMapping(\"/registerNewUserAccount\")");
        String PASSWORD_PATTERN =
                "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%^&+=!]).{8,}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(userDto.getPassword());
        if (!matcher.matches()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        try {

            System.out.println("create new user");
            Users user = new Users();
            System.out.println(user.getId());
            user.setEmail(userDto.getEmail());
            System.out.println(user.getEmail());
            user.setPhone(userDto.getPhone());
            System.out.println(user.getPhone());
            user.setUsername(userDto.getUserName());
            System.out.println(user.getUsername());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            System.out.println(user.getPassword());
            user.setRole(userDto.getRole());
            System.out.println(user.getRole());
            Users savedUser = userRepository.save(user);
            System.out.println("new id is"+user.getId());
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users loginRequest) {
        Users user = userRepository.findByEmail(loginRequest.getEmail());

        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        org.springframework.security.core.userdetails.User springUser =
                new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(springUser, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String token = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            System.out.println("in validate");
            System.out.println("get token -" + token + "-");
            String jwt = token.substring(7);
            System.out.println("remove Bearer -" + jwt + "-");
            if (jwtUtil.validateToken(jwt)) {
                return ResponseEntity.ok(Collections.singletonMap("valid", true));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token validation failed");
        }
    }


    @GetMapping("/getUser/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        System.out.println("Im in /getUser/{email}");
        Users user = userRepository.findByEmail(email);
        System.out.println("User phon is : " + user.getPhone());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found 2* email");
        }
        System.out.println("UserDto ans_user = new UserDto();");
        UserDto ans_user = new UserDto();
        ans_user.setId(user.getId());
        ans_user.setUsername(user.getUsername());
        ans_user.setEmail(user.getEmail());
        ans_user.setPhone(user.getPhone());
        ans_user.setRole(user.getRole());
        ans_user.setPassword(null);
        System.out.println("return ResponseEntity.ok(ans_user);");
        return ResponseEntity.ok(ans_user);
    }

    @GetMapping("/getUserId")
    public ResponseEntity<?> getUserId(@RequestHeader("Authorization") String token) {
        System.out.println("1  GetMapping(\"/userId\") -" + token + "-");

        try {
            System.out.println("2 GetMapping(\"/userId\") -" + token + "-");

            String jwt = token.substring(7);

            if (jwtUtil.validateToken(jwt)) {
                String email = jwtUtil.extractEmail(jwt);
                System.out.println("@GetMapping(\"/email\") email ==  -" + email + "-");
                Users user = userRepository.findByEmail(email);
                System.out.println("userRepository.findByEmail(email).getId() ==  -" + userRepository.findByEmail(email).getId() + "-");

                if (user == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                }
                return ResponseEntity.ok(user.getId());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token validation failed");
        }
    }

    @GetMapping("/get-email")
    public ResponseEntity<?> getEmailFromToken(@RequestHeader("Authorization") String token) {
        System.out.println("1  GetMapping(\"/email\") -" + token + "-");

        try {
            System.out.println("2 GetMapping(\"/email\") -" + token + "-");

            String jwt = token.substring(7);

            if (jwtUtil.validateToken(jwt)) {
                String email = jwtUtil.extractEmail(jwt);
                System.out.println("@GetMapping(\"/email\") email ==  -" + email + "-");

                return ResponseEntity.ok(email);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token validation failed");
        }
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
