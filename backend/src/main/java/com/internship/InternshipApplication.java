package com.internship;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.internship.mapper")
public class InternshipApplication {
    public static void main(String[] args) {
        SpringApplication.run(InternshipApplication.class, args);
    }
}
