package com.example.studyrediswithjwt.service;

import com.example.studyrediswithjwt.model.User;
import com.example.studyrediswithjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordTestService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public void testPassword() {
        User admin = userRepository.findByUsername("admin").orElse(null);
        if (admin != null) {
            String rawPassword = "admin123";
            String encodedPassword = admin.getPassword();

            boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
            System.out.println("비밀번호 매치 결과: " + matches);
            System.out.println("원본 비밀번호: " + rawPassword);
            System.out.println("저장된 해시: " + encodedPassword);
        }
    }
}