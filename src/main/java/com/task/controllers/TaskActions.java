package com.task.manager;

import com.task.DTO.ListObjectRequest;
import com.task.entities.ListObject;
//import com.task.entities.TaskTable;
import com.task.repositories.TaskRepository;
//import com.task.repositories.TaskTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.Optional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tasks")
public class TaskActions {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/create")
    public void createTask(@RequestBody ListObjectRequest request) {
        ListObject listObject = new ListObject();
//        TaskTable task = new TaskTable();
//        listObject.setTask(task);
        listObject.setTitle(request.getTitle());
        listObject.setDescription(request.getDescription());
        listObject.setStartTime(request.getStartTime());
        listObject.setPriority(request.getPriority());
        listObject.setStatus(request.getStatus());
        listObject.setEndTime(request.getStartTime(), request.getDays(), request.getHours(), request.getMinutes());
        taskRepository.save(listObject);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ListObject> changeStatus(@PathVariable Long id, @RequestBody ListObjectRequest request) {
        Optional<ListObject> object = taskRepository.findById(id);
        if (object.isPresent()) {
            ListObject listObject = object.get();
            listObject.setStatus(request.getStatus());
            taskRepository.save(listObject);
            return ResponseEntity.ok(listObject);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            taskRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}



