package com.task.note.controllers;

import com.task.DTO.ListObjectRequest;
import com.task.note.DTO.NotesRequest;
import com.task.note.entities.Note;
import com.task.note.repositories.NoteRepository;
import com.task.entities.ListObject;
import com.task.repositories.TaskRepository;
import com.user.auth.Security.JwtUtil;
import com.user.entities.Users;
import com.user.repositories.UserRepository;
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
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<?> createNote(@RequestHeader("Authorization") String token, @RequestBody NotesRequest request) {
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
        ListObject listObject = taskRepository.findById(request.getTask_id())
                .orElse(null);
        if (listObject == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ListObject not found");
        }
        Note note = new Note();
        createNoteObject(user,note,listObject,request);


        noteRepository.save(note);
        return ResponseEntity.ok(note);
    }

    @PostMapping("/createByAdmin")
    public ResponseEntity<?> createTaskByAdmin(@RequestBody NotesRequest request) {
        Note note = new Note();

        Users user = userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ListObject listObject = taskRepository.findById(request.getTask_id())
                .orElse(null);
        if (listObject == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ListObject not found");
        }

        createNoteObject(user,note,listObject,request);
        taskRepository.save(listObject);
        return ResponseEntity.ok(note);

    }

    @PatchMapping("/{id}/{data}/Edit")
    public ResponseEntity<?> edit(@RequestHeader("Authorization") String token, @PathVariable Long id, @PathVariable String data, @RequestBody ListObjectRequest request) {
        String email = "";
        try {
            email = jwtUtil.extractUsername(token.substring(7));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
        Users user = userRepository.findByEmail(email);
        Note note = noteRepository.findById(id).get();
        if (noteRepository.existsById(id) && (user.getId() == note.getUserId() || user.getRole().equals("ADMIN"))) {
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
            email = jwtUtil.extractUsername(token.substring(7));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
        Users user = userRepository.findByEmail(email);
        Note note = noteRepository.findById(id).get();
        if (noteRepository.existsById(id) && (user.getId() == note.getUserId()||user.getRole().equals("ADMIN"))) {
            noteRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    private void createNoteObject(Users user,Note note,ListObject listObject, NotesRequest request){
        note.setTitle(request.getTitle());
        note.setDescription(request.getDescription());
        note.setSendTime(LocalDateTime.now());
        note.setUserId(user.getId());
        note.setListObject(listObject);
    }

}



