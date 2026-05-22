package com.fittrack.analytics_core.service;

import com.fittrack.analytics_core.model.User;
import com.fittrack.analytics_core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. Check if user exists
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // 2. Find user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 3. Register a new user (Business logic: Hashing password happens here!)
    public User registerNewUser(String email, String rawPassword) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(rawPassword)); // Secure hashing
        
        return userRepository.save(newUser);
    }
}