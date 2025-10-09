package com.example.studyrediswithjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StudyRedisWithJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyRedisWithJwtApplication.class, args);
    }

}
