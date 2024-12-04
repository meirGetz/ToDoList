package com.task.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ביטול CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/tasks/create").permitAll() // התרת גישה לנתיב
                        .anyRequest().authenticated() // כל שאר הבקשות דורשות אימות
                );
        return http.build();
    }
}
