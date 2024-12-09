package com.task.Security;

import com.task.Security.CustomUserDetailsService;
import com.task.Security.JwtAuthenticationFilter;
import com.task.Security.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtUtils jwtUtils, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtils, customUserDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ביטול CSRF
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/tasks/create").hasRole("USER")
                        .requestMatchers("/api/tasks/create").permitAll()
                        .requestMatchers("/api/tasks/{id}/status").permitAll()
                        .requestMatchers("/api/tasks/{id}/delete").permitAll()
                        .requestMatchers("/api/user/registerNewUserAccount").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // הוספת הפילטר לפני Authentication
        return http.build();
    }
}
