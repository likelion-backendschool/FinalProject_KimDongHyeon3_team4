package com.example.finalproject2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Finalproject2Application {

    public static void main(String[] args) {
        SpringApplication.run(Finalproject2Application.class, args);
    }

}
