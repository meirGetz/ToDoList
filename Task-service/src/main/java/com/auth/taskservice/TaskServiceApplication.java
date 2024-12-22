package com.auth.taskservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.auth.taskservice", "com.todolist.commonmodule"})
@EnableJpaRepositories(basePackages = "com.todolist.commonmodule.repositories")
@EntityScan(basePackages = "com.todolist.commonmodule.entities")


public class TaskServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskServiceApplication.class, args);
    }

}
