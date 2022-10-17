package com.example.finalproject1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Finalproject1Application {
    public static void main(String[] args) {
        SpringApplication.run(Finalproject1Application.class, args);
    }
}
