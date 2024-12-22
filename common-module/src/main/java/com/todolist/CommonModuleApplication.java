package com.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.todolist.commonmodule.repositories")
@ComponentScan(basePackages = "com.todolist.commonmodule")
@EntityScan(basePackages = "com.todolist.commonmodule.entities")
public class CommonModuleApplication {

    public static void main(String[] args) {

        SpringApplication.run(CommonModuleApplication.class, args);
    }

}
