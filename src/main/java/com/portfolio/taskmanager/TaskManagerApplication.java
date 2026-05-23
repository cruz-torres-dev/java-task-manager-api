package com.portfolio.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point of the Task Manager API application.
 *
 * The @SpringBootApplication annotation combines three annotations:
 * - @Configuration: Marks this class as a source of bean definitions.
 * - @EnableAutoConfiguration: Tells Spring Boot to auto-configure beans based on dependencies.
 * - @ComponentScan: Scans this package and sub-packages for Spring components.
 */
@SpringBootApplication
public class TaskManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagerApplication.class, args);
    }

}
