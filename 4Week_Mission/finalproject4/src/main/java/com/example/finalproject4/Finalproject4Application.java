package com.example.finalproject4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Finalproject4Application {

    public static void main(String[] args) {
        SpringApplication.run(Finalproject4Application.class, args);
    }

}
