package com.fittrack.analytics_core.controller;

import com.fittrack.analytics_core.model.User;
import com.fittrack.analytics_core.model.UserProfileDocument;
import com.fittrack.analytics_core.repository.UserProfileRepository;
import com.fittrack.analytics_core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/features")
@CrossOrigin(origins = "*")
public class HealthFeaturesController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private RestTemplate restTemplate;

    // 🎯 DIRECT PRODUCTION LINK: Hardcoded live cloud Python API domain
    private final String pythonBaseUrl = "https://bulk-fitness-ml.onrender.com";

    // --------------------------------------------------------
    // API ENDPOINT 3: FETCH HISTORICAL TRACKING DATA
    // --------------------------------------------------------
    @GetMapping("/history")
    public ResponseEntity<?> getUserTrackingHistory() {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> currentUser = userService.findByEmail(loggedInEmail);

        if (currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access.");
        }

        // Fetch all past health entries for this logged-in user sorted by timestamp
        List<UserProfileDocument> history = userProfileRepository.findByUserIdOrderByTimestampDesc(currentUser.get().getId());
        return ResponseEntity.ok(history);
    }


    // --------------------------------------------------------
    // API ENDPOINT 4: DYNAMIC CALORIE HANDLER (Proxy to Python)
    // --------------------------------------------------------
    @PostMapping("/calculate-food")
    public ResponseEntity<?> proxyCalorieCalculation(@RequestBody Map<String, Object> requestPayload) {
        // Dynamically construct URL path without needing environment lookups
        String finalPythonTargetUrl = pythonBaseUrl + "/calculate-calories";

        try {
            // Forward the raw food key and quantities to our live cloud Python server instance
            @SuppressWarnings("unchecked")
            Map<String, Object> pythonResponse = restTemplate.postForObject(finalPythonTargetUrl, requestPayload, Map.class);
            return ResponseEntity.ok(pythonResponse);
        } catch (Exception e) {
            System.err.println("Cloud Routing Handshake Exception: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Calorie Calculation Engine is offline.", "details", e.getMessage()));
        }
    }
}