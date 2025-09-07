package com.example.studyrediswithjwt.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHashGenerator {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PasswordHashGenerator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void generateHashes() {
        System.out.println("admin123 해시: " + passwordEncoder.encode("admin123"));
        System.out.println("password123 해시: " + passwordEncoder.encode("password123"));
        System.out.println("password456 해시: " + passwordEncoder.encode("password456"));
        System.out.println("user123 해시: " + passwordEncoder.encode("user123"));
    }
}