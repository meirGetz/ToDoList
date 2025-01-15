package com.task.service;

import com.DTO.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    private final String USER_SERVICE_URL = "http://user-service:8081/api/user"; // כתובת ה-User Service

    public UserDto getUserById(long id) {
        String url = USER_SERVICE_URL + "/" + id;
        try {
            return restTemplate.getForObject(url, UserDto.class);
        } catch (Exception e) {
            // טיפול בשגיאות, אפשר להוסיף לוגים או טיפול אחר במקרה של שגיאה
            return null; // במקרה של שגיאה בהתקשרות
        }
    }

    public UserDto getUserByToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            System.out.println("get getUser");
            System.out.println("token"+token);
            String email = getEmailFromToken(token);
            ResponseEntity<UserDto> response = restTemplate.exchange(
                    USER_SERVICE_URL + "/getUser/"+email, HttpMethod.GET, entity, UserDto.class);
            System.out.println("response getPhone = = = " + response.getBody().getPhone());

            System.out.println("response.getBody() = " + response.getBody());
            return response.getBody(); // מחזיר את ה-email ישירות
        } catch (Exception e) {
            return null;
        }
    }

    public String getEmailFromToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    USER_SERVICE_URL + "/get-email", HttpMethod.GET, entity, String.class);
            System.out.println("response.getBody() = " + response.getBody());
            return response.getBody(); // מחזיר את ה-email ישירות
        } catch (Exception e) {
            return null;
        }
    }

    public Long getUserId(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            System.out.println("hho");

            ResponseEntity<String> response = restTemplate.exchange(
                    USER_SERVICE_URL + "/getUserId", HttpMethod.GET, entity, String.class);
            System.out.println("wwe");
            if (response.getBody() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    System.out.println("response.getBody()"+response.getBody());
                    System.out.println("Long userId = "+objectMapper.readValue(response.getBody(), Long.class));
                    Long userId = objectMapper.readValue(response.getBody(), Long.class);
                    System.out.println("userId = "+userId);

                    return userId;
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return (long)-1;  // או ערך אחר במקרה של שגיאה בהמרה
                }
            } else {
                return (long) -1;  // במקרה שבו אין גוף בתגובה
            }
        } catch (Exception e) {
            return (long)-1;  // במקרה של שגיאה כלשהי בתקשורת
        }
    }


}
