package com.task.service;

import com.DTO.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

@Service
public class AuthService {

    @Autowired
    private RestTemplate restTemplate;

    private final String USER_SERVICE_URL = "http://user-service:8081/api/user"; // כתובת ה-User Service לאימות ה-JWT

    public boolean validateToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        System.out.println("befor set -"+token+"-");
        headers.set("Authorization", "Bearer " + token);
        // הוספת ה-Token לכותרת ה-HTTP
        System.out.println("after set -"+token+"-");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            System.out.println("befor exchange -"+token+"-");
            System.out.println("\nUSER_SERVICE_URL+\"/validate\" -"+USER_SERVICE_URL+"/validate\n"+"-");

            restTemplate.exchange(USER_SERVICE_URL+"/validate", HttpMethod.GET, entity, String.class);
            return true; // אם אין שגיאה, ה-token תקף
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return false; // במקרה של שגיאה, ה-token לא תקף

        }
    }

}
