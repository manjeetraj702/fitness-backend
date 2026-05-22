package com.fittrack.analytics_core.service;

import com.fittrack.analytics_core.model.UserProfileDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final RestTemplate restTemplate = new RestTemplate();

    // 🎯 DIRECT PRODUCTION LINK: Hardcoded live cloud ML service route
    private final String pythonMlUrl = "https://bulk-fitness-ml.onrender.com/generate-plan";

    @SuppressWarnings("unchecked")
    public UserProfileDocument processAndSaveAnalytics(UserProfileDocument profile) {

        // 1. Build validation payload matching Python's Pydantic properties exactly
        Map<String, Object> pythonRequest = new HashMap<>();
        pythonRequest.put("age", profile.getAge());
        pythonRequest.put("weightKg", profile.getWeightKg());
        pythonRequest.put("heightCm", profile.getHeightCm());
        pythonRequest.put("optimizationGoal", profile.getOptimizationGoal());

        // 2. Direct Network exchange pointing to your live Render endpoint url
        Map<String, Object> mlResult = restTemplate.postForObject(pythonMlUrl, pythonRequest, Map.class);

        // 3. Assemble nested structures to prevent UI "undefined" errors
        Map<String, Object> nestedPredictions = new HashMap<>();

        if (mlResult != null) {
            nestedPredictions.put("dailyCalories", mlResult.get("calories"));
            nestedPredictions.put("macros", Map.of(
                    "protein", mlResult.get("protein"),
                    "carbs", mlResult.get("carbs"),
                    "fats", mlResult.get("fats")
            ));
        }

        // 4. Handle dynamic training updates based on target indicators
        String goal = profile.getOptimizationGoal().toLowerCase();

        if (goal.contains("cut")) {
            nestedPredictions.put("workoutPlan", Map.of(
                    "frequency", "4 Days / Week", "focus", "High Reps & Fast Fat Burn 🔥",
                    "days", List.of(
                            Map.of("name", "👕 Day 1: Chest Workout", "routines", List.of("Flat Barbell Bench Press: 4x12", "Incline Dumbbell Press: 3x12")),
                            Map.of("name", "💪 Day 2: Back & Biceps", "routines", List.of("Lat Pulldowns: 4x12", "Dumbbell Bicep Curls: 4x15"))
                    )
            ));
            nestedPredictions.put("mealPlan", Map.of(
                    "strategy", "Caloric Deficit & Fat Loss Burn 🔥",
                    "days", List.of(
                            Map.of("time", "🍳 Breakfast Entry", "dish", "Egg White Masala Omelette", "details", "5 Egg Whites + 2 slices Brown Bread.")
                    )
            ));
        } else {
            nestedPredictions.put("workoutPlan", Map.of(
                    "frequency", "4 Days / Week", "focus", "Heavy Weight & Size Accumulation 🏗️",
                    "days", List.of(
                            Map.of("name", "👕 Day 1: Heavy Chest Day", "routines", List.of("Flat Barbell Bench Press: 4x8 (Heavy)")),
                            Map.of("name", "🦵 Day 2: Squat Heavy Drive", "routines", List.of("Barbell Back Squats: 4x8 (Heavy)"))
                    )
            ));
            nestedPredictions.put("mealPlan", Map.of(
                    "strategy", "Caloric Surpluses & Size Building 🏗️",
                    "days", List.of(
                            Map.of("time", "🍳 Breakfast Entry", "dish", "Peanut Butter Banana Oats Shake", "details", "60g Oats + 2 tbsp Peanut Butter.")
                    )
            ));
        }

        nestedPredictions.put("algorithmUsed", "Ensemble Random Forest Multi-Output Regressor Matrix");

        // 5. Commit object mappings down to MongoDB model layer
        profile.setAnalyticalPredictions(nestedPredictions);

        return profile;
    }
}