package com.example.studyrediswithjwt.service;

import com.example.studyrediswithjwt.model.User;
import com.example.studyrediswithjwt.repository.UserRepository;
import com.example.studyrediswithjwt.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "user", key = "#id")
    public Optional<UserDto> getUserById(Long id) {

        System.out.println("UserService.getUserById: " + id);

        return userRepository.findById(id)
                .map(this::convertToDto);
    }

    public UserDto createUser(UserDto userDto) {
        User user = convertToEntity(userDto);
        // 비밀번호 인코딩
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @CachePut(value = "user", key = "#userDto.id")
    public Optional<UserDto> updateUser(Long id, UserDto userDto) {
        if (!userRepository.existsById(id)) {
            return Optional.empty();
        }

        User user = convertToEntity(userDto);
        user.setId(id);
        // 비밀번호가 제공된 경우에만 인코딩
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        User updatedUser = userRepository.save(user);
        return Optional.of(convertToDto(updatedUser));
    }

    @CacheEvict(value = "user", key = "#id")
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                // 보안상 비밀번호는 DTO에서 제외
                .build();
    }

    private User convertToEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .phone(userDto.getPhone())
                .build();
    }
}
