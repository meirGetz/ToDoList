package com;

import com.task.manager.CreateTask;
import com.task.model.ListObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        CreateTask task = new CreateTask();
        //only for test
        ListObject new_task = task.createTask();
        System.out.println(new_task.getTitle());
        System.out.println(new_task.getDescription());
        System.out.println(new_task.getStartTime());
        System.out.println(new_task.getEndTime());
    }
}