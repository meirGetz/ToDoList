package com.user.controllers;

import com.DTO.LoginRequest;
import com.DTO.UserDto;
import com.user.auth.Security.CustomUserDetailsService;
import com.user.auth.Security.JwtUtil;
import com.user.Service.UserService;
import com.user.entities.Users;
import com.user.repositories.UserRepository;
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

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(springUser, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        String token = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            System.out.println("in validate -"+token+"-");

            System.out.println(token);
            String jwt = token.substring(7); // הסרת "Bearer "
            System.out.println("remove Bearer -"+token+"-");

            System.out.println("jwt="+jwt);
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
        System.out.println("User phon is : "+user.getPhone());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found 2* email");
        }
        System.out.println("UserDto ans_user = new UserDto();");
        // יצירת אובייקט UserDto
        UserDto ans_user = new UserDto();
        ans_user.setId(user.getId());
        ans_user.setFirstName(user.getUsername());
        ans_user.setLastName(user.getUsername());
        ans_user.setEmail(user.getEmail());
        ans_user.setPhone(user.getPhone());
        ans_user.setRole(user.getRole());
        ans_user.setPassword(null);
        ans_user.setMatchingPassword(null);
        System.out.println("return ResponseEntity.ok(ans_user);");
        return ResponseEntity.ok(ans_user);
    }

    @GetMapping("/getUserId")
    public ResponseEntity<?> getUserId(@RequestHeader("Authorization") String token) {
        System.out.println("1  GetMapping(\"/userId\") -"+token+"-");

        try {
            System.out.println("2 GetMapping(\"/userId\") -"+token+"-");

            String jwt = token.substring(7); // הסרת "Bearer " מה-token

            if (jwtUtil.validateToken(jwt)) {
                String email = jwtUtil.extractUsername(jwt); // חילוץ ה-email מה-token
                System.out.println("@GetMapping(\"/email\") email ==  -"+email+"-");
                Users user = userRepository.findByEmail(email);
                System.out.println("userRepository.findByEmail(email).getId() ==  -"+userRepository.findByEmail(email).getId()+"-");

                if (user == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                }
                return ResponseEntity.ok( user.getId());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token validation failed");
        }
    }

    @GetMapping("/get-email")
    public ResponseEntity<?> getEmailFromToken(@RequestHeader("Authorization") String token) {
        System.out.println("1  GetMapping(\"/email\") -"+token+"-");

        try {
            System.out.println("2 GetMapping(\"/email\") -"+token+"-");

            String jwt = token.substring(7); // הסרת "Bearer " מה-token

            if (jwtUtil.validateToken(jwt)) {
                String email = jwtUtil.extractUsername(jwt); // חילוץ ה-email מה-token
                System.out.println("@GetMapping(\"/email\") email ==  -"+email+"-");

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
