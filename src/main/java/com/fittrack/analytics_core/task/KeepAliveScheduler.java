package com.fittrack.analytics_core.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KeepAliveScheduler {

    private final RestTemplate restTemplate = new RestTemplate();

    // Current Render URLs
    private final String selfJavaUrl =
            "https://fitness-backend-1-r8wq.onrender.com/actuator/health";

    private final String pythonMlUrl =
            "https://bulk-fitness-ml-m1xn.onrender.com/";

    @Scheduled(fixedRate = 240000) // 4 minutes
    public void maintainCloudContainersWarmth() {

        System.out.println("💓 Triggering automated cloud lifecycle keep-alive ping sweep...");

        // Keep Spring Boot backend warm
        try {
            String response = restTemplate.getForObject(
                    selfJavaUrl,
                    String.class
            );

            System.out.println(
                    "✅ Java Backend Container Alive: " + response
            );

        } catch (Exception e) {

            System.err.println(
                    "⚠️ Java Backend Ping Failed: " + e.getMessage()
            );
        }

        // Keep Python ML service warm
        try {
            String response = restTemplate.getForObject(
                    pythonMlUrl,
                    String.class
            );

            System.out.println(
                    "✅ Python ML Container Alive: " + response
            );

        } catch (Exception e) {

            System.err.println(
                    "❌ Python ML Ping Failed: " + e.getMessage()
            );
        }
    }
}