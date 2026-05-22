package com.fittrack.analytics_core.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String email;
    
    // We will encrypt this using BCrypt later. NEVER store plain text!
    private String password; 
    
    private String role = "ROLE_USER"; // Standard enterprise role-based access

    private LocalDateTime createdAt = LocalDateTime.now();
}