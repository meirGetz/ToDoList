package com.task.manager;

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
    public void createTask(@RequestBody ListObject listObject) {
        TaskTable task = new TaskTable();

//        // יצירת משימה חדשה
//        TaskTable task = new TaskTable();
//
//        // יצירת ListObject חדש
//        listObject.setTitle("Project meeting");
//        listObject.setDescription("scram");
//        listObject.setPriority(5);
//        listObject.setStartTime(LocalDateTime.parse("2025-12-03T10:15:30"));
//        listObject.setEndTime(listObject.getStartTime(), 4, 4, 4);
//        task.getListObjects().forEach(Object -> listObject.setTask(task));
//
//        // שמירה של המשימה במסד הנתונים
//        taskTableRepository.save(task);  // שמור את המשימה
        // יצירת משימה חדשה

        // הוספת ה-ListObject למשימה
        listObject.setTask(task);  // קביעת הקשר למשימה
        task.getListObjects().add(listObject);  // הוספת ה-ListObject למשימה


        // שמירה של המשימה במסד הנתונים
        taskTableRepository.save(task);
    }
}
