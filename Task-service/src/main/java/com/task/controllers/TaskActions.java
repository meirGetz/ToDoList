package com.task.controllers;

import com.DTO.UserDto;
import com.task.entities.ListObject;
import com.task.repositories.TaskRepository;
import com.task.DTO.ListObjectRequest;
import com.task.service.AuthService;
import com.task.service.UserService; // שירות המשתמש
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskActions {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AuthService authService; // שירות האימות
    @Autowired
    private UserService userService; // שירות המשתמש

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestHeader("Authorization") String token, @RequestBody ListObjectRequest request) {
        System.out.println("create token = " + token);
        if (!authService.validateToken(token.substring(7))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token " + token);
        }
        System.out.println("go to get user by id");
        Long user_id =  userService.getUserId(token.substring(7));
        if (user_id == -1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found!!!");
        }

        ListObject listObject = new ListObject();
        listObject.setTitle(request.getTitle());
        listObject.setDescription(request.getDescription());
        listObject.setStartTime(request.getStartTime());
        listObject.setPriority(request.getPriority());
        listObject.setStatus(request.getStatus() != null ? request.getStatus() : "Pending");
        listObject.setUserId(user_id);
        listObject.setEndTime(request.getStartTime(), request.getDays(), request.getHours(), request.getMinutes());

        taskRepository.save(listObject);
        return ResponseEntity.ok(listObject);
    }

    @PatchMapping("/{id}/{data}/Edit")
    public ResponseEntity<?> edit(@RequestHeader("Authorization") String token, @PathVariable Long id, @PathVariable String data, @RequestBody ListObjectRequest request) {
        String email = "";
        try {
            System.out.println(token);
            email = userService.getEmailFromToken(token.substring(7));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        UserDto user = userService.getUserByToken(token.substring(7)); // קריאה לשירות המשתמש
        ListObject listObject = taskRepository.findById(id).orElse(null);
        if (listObject != null && (user.getId() == listObject.getUserId() || user.getRole().equals("ADMIN"))) {
            switch (data) {
                case "Title":
                    listObject.setTitle(request.getTitle());
                    break;
                case "Description":
                    listObject.setDescription(request.getDescription());
                    break;
                case "Status":
                    listObject.setStatus(request.getStatus());
                    break;
                case "Time":
                    listObject.setStartTime(request.getStartTime());
                    listObject.setEndTime(request.getStartTime(), request.getEndTime());
            }
            taskRepository.save(listObject);
            return ResponseEntity.ok(listObject);
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteTask(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        String email = "";
        try {
            System.out.println(token);
            email = userService.getEmailFromToken(token.substring(7));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        UserDto user = userService.getUserByToken(token.substring(7)); // קריאה לשירות המשתמש
        ListObject listObject = taskRepository.findById(id).orElse(null);
        if (listObject != null && (user.getId() == listObject.getUserId() || user.getRole().equals("ADMIN"))) {
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
            email = userService.getEmailFromToken(token.substring(7));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }



        List<ListObject> tasks = taskRepository.findByUserId(userService.getUserId(token.substring(7)));
        if (tasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(tasks);
    }

}



