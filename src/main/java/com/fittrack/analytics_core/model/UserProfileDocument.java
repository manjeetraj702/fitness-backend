package com.fittrack.analytics_core.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "user_profiles")
public class UserProfileDocument {

    @Id
    private String id;
    private String userId;
    private Integer age;
    private Double weightKg;
    private Double heightCm;
    private String optimizationGoal;

    // 🎯 THE CORE PREDICTIVE CONTAINER: Stores the complete nested dictionary outputs from your ML service
    private Map<String, Object> analyticalPredictions;

    private LocalDateTime timestamp = LocalDateTime.now();

    // ============================================================
    // CONSTRUCTORS
    // ============================================================
    public UserProfileDocument() {
    }

    public UserProfileDocument(String userId, Integer age, Double weightKg, Double heightCm, String optimizationGoal) {
        this.userId = userId;
        this.age = age;
        this.weightKg = weightKg;
        this.heightCm = heightCm;
        this.optimizationGoal = optimizationGoal;
    }

    // ============================================================
    // GETTERS AND SETTERS
    // ============================================================
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public Double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Double heightCm) {
        this.heightCm = heightCm;
    }

    public String getOptimizationGoal() {
        return optimizationGoal;
    }

    public void setOptimizationGoal(String optimizationGoal) {
        this.optimizationGoal = optimizationGoal;
    }

    public Map<String, Object> getAnalyticalPredictions() {
        return analyticalPredictions;
    }

    public void setAnalyticalPredictions(Map<String, Object> analyticalPredictions) {
        this.analyticalPredictions = analyticalPredictions;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // ============================================================
    // TELEMETRY TOSTRING PRINTER
    // ============================================================
    @Override
    public String toString() {
        return "UserProfileDocument{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", age=" + age +
                ", weightKg=" + weightKg +
                ", heightCm=" + heightCm +
                ", optimizationGoal='" + optimizationGoal + '\'' +
                ", analyticalPredictions=" + analyticalPredictions +
                ", timestamp=" + timestamp +
                '}';
    }
}