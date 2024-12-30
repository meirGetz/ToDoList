package com.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = {"com.task.repositories", "com.task.note.repositories"})
@EntityScan(basePackages = {"com.task.entities","com.task.note.entities"})
@SpringBootApplication(scanBasePackages = {"com.task", "com.user", "com.task.note"})
public class TaskServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskServiceApplication.class, args);
    }
}

