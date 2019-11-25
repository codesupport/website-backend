package dev.codesupport.web.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring Boot boilerplate, starts the application.
 *
 * ComponentScan designates at what package level does spring being scanning for components.
 */
@SpringBootApplication
@ComponentScan({"dev.codesupport"})
public class ApiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiServiceApplication.class, args);
    }
}
