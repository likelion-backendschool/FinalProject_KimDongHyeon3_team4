package com.example.finalproject3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Finalproject3Application {

    public static void main(String[] args) {
        SpringApplication.run(Finalproject3Application.class, args);
    }

}
