package com.fittrack.analytics_core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/features")
@CrossOrigin(origins = "*")
public class HealthFeaturesController {

    private final RestTemplate restTemplate = new RestTemplate();

    // 🎯 TARGET LINK: Live hardcoded address pointing to your FastAPI instance
    private final String pythonBaseUrl = "https://bulk-fitness-ml.onrender.com/calculate-calories";

    @PostMapping("/calculate-food")
    public ResponseEntity<?> proxyFoodCalculation(@RequestBody Map<String, Object> requestPayload) {
        try {
            // Forward the payload data object down to your Python container
            @SuppressWarnings("unchecked")
            Map<String, Object> pythonResponse = restTemplate.postForObject(pythonBaseUrl, requestPayload, Map.class);
            return ResponseEntity.ok(pythonResponse);
        } catch (Exception e) {
            System.err.println("❌ Food Calculation Proxy Handshake Failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of("error", "Python nutritional engine dropped the connection threat.", "details", e.getMessage()));
        }
    }
}