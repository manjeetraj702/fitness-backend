package com.fittrack.analytics_core.controller;

import com.fittrack.analytics_core.dto.AuthRequest;
import com.fittrack.analytics_core.model.User;
import com.fittrack.analytics_core.service.UserService;
import com.fittrack.analytics_core.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // --------------------------------------------------------
    // 1. REGISTRATION ENDPOINT
    // --------------------------------------------------------
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest request) {
        // Check if email already exists
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email is already in use!"));
        }

        // The Service layer now safely handles the entity creation and hashing
        userService.registerNewUser(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(Map.of("message", "User registered successfully!"));
    }

    // --------------------------------------------------------
    // 2. LOGIN ENDPOINT
    // --------------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest request) {
        Optional<User> userOptional = userService.findByEmail(request.getEmail());

        // Check if user exists
        if (userOptional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }

        User user = userOptional.get();

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }

        // Generate the JWT Token
        String token = jwtUtil.generateToken(user.getEmail());

        // Return success response with the Token
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful!");
        response.put("userId", user.getId());
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}