package com.task.controllers;

import com.task.DTO.ListObjectRequest;
import com.task.Security.JwtUtil;
import com.task.entities.ListObject;
//import com.task.entities.TaskTable;
import com.task.entities.Users;
import com.task.repositories.TaskRepository;
//import com.task.repositories.TaskTableRepository;
import com.task.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskActions {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestHeader("Authorization") String token, @RequestBody ListObjectRequest request) {
        String email;
        try {
            email = jwtUtil.extractUsername(token.substring(7)); // הסר את "Bearer " מהכותרת
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        Users user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        ListObject listObject = new ListObject();
        listObject.setTitle(request.getTitle());
        listObject.setDescription(request.getDescription());
        listObject.setStartTime(request.getStartTime());
        listObject.setPriority(request.getPriority());
        listObject.setStatus(request.getStatus() != null ? request.getStatus() : "Pending");
        listObject.setUserId(user.getId());

        listObject.setEndTime(request.getStartTime(), request.getDays(), request.getHours(), request.getMinutes());

        taskRepository.save(listObject);
        return ResponseEntity.ok(listObject);
    }

    @PostMapping("/createByAdmin")
    public void createTaskByAdmin(@RequestBody ListObjectRequest request) {
        ListObject listObject = new ListObject();

        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        listObject.setTitle(request.getTitle());
        listObject.setDescription(request.getDescription());
        listObject.setStartTime(request.getStartTime());
        listObject.setPriority(request.getPriority());
        listObject.setStatus(request.getStatus() != null ? request.getStatus() : "Pending");
        listObject.setUserId(user.getId());

        listObject.setEndTime(request.getStartTime(), request.getDays(), request.getHours(), request.getMinutes());

        taskRepository.save(listObject);
    }


    @PatchMapping("/{id}/editStatus")
    public ResponseEntity<?> changeStatus(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody ListObjectRequest request) {
        String email = "";
        try {
            email = jwtUtil.extractUsername(token.substring(7));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
        Users user = userRepository.findByEmail(email);
        ListObject listObject = taskRepository.findById(id).get();
//        Optional<ListObject> object = taskRepository.findById(id);
        if (taskRepository.existsById(id) && (user.getId() == listObject.getUserId()||user.getRole().equals("ADMIN"))) {
//            ListObject listObject = object.get();
            listObject.setStatus(request.getStatus());
            taskRepository.save(listObject);
            return ResponseEntity.ok(listObject);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteTask(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        String email = "";
        try {
            email = jwtUtil.extractUsername(token.substring(7));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
        Users user = userRepository.findByEmail(email);
        ListObject listObject = taskRepository.findById(id).get();
        if (taskRepository.existsById(id) && (user.getId() == listObject.getUserId()||user.getRole().equals("ADMIN"))) {
            taskRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    @GetMapping("/taskView")
    public ResponseEntity<?> getTasks(@RequestHeader("Authorization") String token) {
        String email = "";
        try {
            email = jwtUtil.extractUsername(token.substring(7));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        Users user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        List<ListObject> tasks = taskRepository.findByUserId(user.getId());
        if (tasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(tasks);
    }

}



