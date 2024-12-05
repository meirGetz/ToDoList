package com.task.manager;

import com.task.DTO.ListObjectRequest;
import com.task.entities.ListObject;
import com.task.entities.TaskTable;
import com.task.repositories.TaskTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tasks")
public class CreateTask {

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
        listObject.setEndTime(request.getStartTime(),request.getDays(),request.getHours(),request.getMinutes());
        task.getListObjects().add(listObject);
        taskTableRepository.save(task);
    }
}
