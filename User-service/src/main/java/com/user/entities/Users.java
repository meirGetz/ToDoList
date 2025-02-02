package com.user.entities;

import com.DTO.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.user.auth.Validation.ValidEmail;
import com.user.auth.Validation.ValidPassword;
import com.user.auth.Validation.ValidPhoneNumber;
import jakarta.persistence.*;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    @ValidPassword
    @Column(unique = false, nullable = false)
    private String password;

    @ValidEmail
    @Column(unique = true, nullable = false)
    private String email;
    @ValidPhoneNumber
    @Column(unique = true, nullable = false)
    private String phone;

    private boolean enabled;
    @Column(unique = false, nullable = false)
    private String role;

    public Users() {

    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }

    public void copyFromDto(UserDto dto) {
        this.setId(dto.getId());
        this.setEmail(dto.getEmail());
        this.setPhone(dto.getPhone());
        this.setPassword(dto.getPassword());
        this.setRole(dto.getRole());
        this.setUsername(dto.getUserName());
    }

}