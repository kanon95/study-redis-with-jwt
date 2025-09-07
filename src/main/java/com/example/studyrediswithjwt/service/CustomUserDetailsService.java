package com.example.studyrediswithjwt.service;

import com.example.studyrediswithjwt.model.User;
import com.example.studyrediswithjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.logging.Logger;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final Logger logger = Logger.getLogger(CustomUserDetailsService.class.getName());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("로그인 시도: " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warning("사용자를 찾을 수 없음: " + username);
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
                });

        logger.info("사용자 찾음: " + username + ", 암호화된 비밀번호 길이: " + user.getPassword().length());

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_USER") // 기본적으로 USER 권한 부여
                .build();

        logger.info("UserDetails 생성 완료: " + userDetails.getUsername());
        return userDetails;
    }
}
