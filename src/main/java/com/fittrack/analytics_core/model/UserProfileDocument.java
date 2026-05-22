package com.fittrack.analytics_core.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document(collection = "health_analytics_profiles")
public class UserProfileDocument {

    @Id
    private String id;

    // IMPORTANT: This links the health record to the user who created it!
    private String userId; 

    private int age;
    private double weightKg;
    private double heightCm;
    private String optimizationGoal; 

    private Map<String, Object> analyticalPredictions;
    private LocalDateTime timestamp = LocalDateTime.now();
}