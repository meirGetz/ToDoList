package com.task.note.controllers;

import com.task.DTO.ListObjectRequest;
import com.task.note.DTO.NotesRequest;
import com.task.note.entities.Note;
import com.task.note.repositories.NoteRepository;
import com.task.entities.ListObject;
import com.task.repositories.TaskRepository;
import com.task.service.AuthService; // שירות האימות
import com.task.service.UserService; // שירות המשתמש
import com.DTO.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/notes")
public class NoteControllers {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private AuthService authService; // שירות האימות
    @Autowired
    private UserService userService; // שירות המשתמש

    @PostMapping("/create")
    public ResponseEntity<?> createNote(@RequestHeader("Authorization") String token, @RequestBody NotesRequest request) {
        if (!authService.validateToken(token.substring(7))) { // הסר את "Bearer " מהכותרת
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        UserDto user = userService.getUserByToken(token.substring(7));
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found  note 1");
        }

        ListObject listObject = taskRepository.findById(request.getTask_id())
                .orElse(null);
        if (listObject == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ListObject not found");
        }

        Note note = new Note();
        createNoteObject(user, note, listObject, request);

        noteRepository.save(note);
        return ResponseEntity.ok(note);
    }

    @PostMapping("/createByAdmin")
    public ResponseEntity<?> createTaskByAdmin(@RequestBody NotesRequest request) {
        Note note = new Note();

        UserDto user = userService.getUserById(request.getUser_id());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found note 2");
        }

        ListObject listObject = taskRepository.findById(request.getTask_id())
                .orElse(null);
        if (listObject == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ListObject not found");
        }

        createNoteObject(user, note, listObject, request);
        taskRepository.save(listObject);
        return ResponseEntity.ok(note);
    }

    @PatchMapping("/{id}/{data}/Edit")
    public ResponseEntity<?> edit(@RequestHeader("Authorization") String token, @PathVariable Long id, @PathVariable String data, @RequestBody ListObjectRequest request) {
        String email = "";
        try {
            email = userService.getEmailFromToken(token.substring(7)); // הוצא את ה-email אחרי אימות
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        UserDto user = userService.getUserByToken(token); // קריאה לשירות המשתמש
        Note note = noteRepository.findById(id).orElse(null);
        if (note != null && (user.getId() == note.getUserId() || user.getRole().equals("ADMIN"))) {
            switch (data) {
                case "Title":
                    note.setTitle(request.getTitle());
                    break;
                case "Description":
                    note.setDescription(request.getDescription());
                    break;
            }
            noteRepository.save(note);
            return ResponseEntity.ok(note);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/Note")
    public ResponseEntity<?> deleteNote(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        String email = "";
        try {
            email = userService.getEmailFromToken(token.substring(7)); // הוצא את ה-email אחרי אימות
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        UserDto user = userService.getUserByToken(token); // קריאה לשירות המשתמש
        Note note = noteRepository.findById(id).orElse(null);
        if (note != null && (user.getId() == note.getUserId() || user.getRole().equals("ADMIN"))) {
            noteRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build(); // 404 Not Found
    }

    private void createNoteObject(UserDto user, Note note, ListObject listObject, NotesRequest request) {
        note.setTitle(request.getTitle());
        note.setDescription(request.getDescription());
        note.setSendTime(LocalDateTime.now());
        note.setUserId(user.getId());
        note.setListObject(listObject);
    }
}
