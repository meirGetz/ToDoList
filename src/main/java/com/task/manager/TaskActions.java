package com.task.manager;

import com.task.DTO.ListObjectRequest;
import com.task.entities.ListObject;
import com.task.entities.TaskTable;
import com.task.repositories.TaskTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.Optional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tasks")
public class TaskActions {

    @Autowired
    private TaskTableRepository taskTableRepository;  // השתמש ב-TaskTableRepository

    @PostMapping("/create")
    public void createTask(@RequestBody ListObjectRequest request) {
        ListObject listObject = new ListObject();
        TaskTable task = new TaskTable();
        listObject.setTask(task);
        listObject.setTitle(request.getTitle());
        listObject.setDescription(request.getDescription());
        listObject.setStartTime(request.getStartTime());
        listObject.setPriority(request.getPriority());
        listObject.setStatus(request.getStatus());
        listObject.setEndTime(request.getStartTime(), request.getDays(), request.getHours(), request.getMinutes());
        task.getListObjects().add(listObject);
        taskTableRepository.save(task);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ListObject> changeStatus(@PathVariable Long id, @RequestBody ListObjectRequest request) {
        Optional<TaskTable> taskOptional = taskTableRepository.findById(id);
        if (taskOptional.isPresent()) {
            TaskTable task = taskOptional.get();
            task.setStatus(request.getStatus());
            taskTableRepository.save(task);
            return ResponseEntity.ok(task.getListObjects().get(0));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
