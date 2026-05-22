package com.fittrack.analytics_core.controller;

import com.fittrack.analytics_core.model.User;
import com.fittrack.analytics_core.model.UserProfileDocument;
import com.fittrack.analytics_core.service.AnalyticsService;
import com.fittrack.analytics_core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
@CrossOrigin(origins = "*")
public class FitTrackController {

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private UserService userService;

    @PostMapping("/predict")
    public ResponseEntity<?> executePredictivePipeline(@RequestBody UserProfileDocument profile) {
        try {
            // Extracts current context principal security metadata credentials
            String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> currentUser = userService.findByEmail(loggedInEmail);

            if (currentUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User authentication token payload invalid."));
            }

            // Sync structural database document keys
            profile.setUserId(currentUser.get().getId());

            // Pass down to processing orchestration thread layers
            UserProfileDocument processedData = analyticsService.processAndSaveAnalytics(profile);

            return ResponseEntity.ok(processedData);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal Gateway Error: " + e.getMessage()));
        }
    }
}