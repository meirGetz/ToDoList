package com.task.service;
import com.DTO.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class TaskService {

    private final RestTemplate restTemplate;

    @Autowired
    public TaskService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
