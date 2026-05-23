package com.fittrack.analytics_core.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KeepAliveScheduler {

    private final RestTemplate restTemplate = new RestTemplate();

    // 🎯 TARGET STRATEGY: Cross-ping both web nodes in a single scheduled execution sweep!
    private final String selfJavaUrl = "https://fitness-backend-b8r0.onrender.com/actuator/health"; // update to match any root endpoint
    private final String pythonMlUrl = "https://bulk-fitness-ml.onrender.com/";


    @Scheduled(fixedRate = 300000)
    public void maintainCloudContainersWarmth() {
        System.out.println("💓 Triggering automated cloud lifecycle keep-alive ping sweep...");

        // 1. Keep the Java container awake
        try {
            restTemplate.getForObject(selfJavaUrl, String.class);
            System.out.println("✅ Java Gateway Container: Self-Stabilization Complete.");
        } catch (Exception e) {
            System.err.println("⚠️ Java local heartbeat ping skipped (Expected if endpoint restricted): " + e.getMessage());
        }

        // 2. Cross-ping the Python container to keep it warm for the frontend
        try {
            restTemplate.getForObject(pythonMlUrl, String.class);
            System.out.println("✅ Python ML Service Container: Cross-Stabilization Complete.");
        } catch (Exception e) {
            System.err.println("❌ Python container wake loop exception: " + e.getMessage());
        }
    }
}