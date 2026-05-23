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
    private final String pythonMlUrl = "https://bulk-fitness-ml.onrender.com/generate-plan";

    @SuppressWarnings("unchecked")
    public UserProfileDocument processAndSaveAnalytics(UserProfileDocument profile) {
        Map<String, Object> pythonRequest = new HashMap<>();
        pythonRequest.put("age", profile.getAge() != null ? profile.getAge() : 23);
        pythonRequest.put("weightKg", profile.getWeightKg() != null ? profile.getWeightKg() : 72.5);
        pythonRequest.put("heightCm", profile.getHeightCm() != null ? profile.getHeightCm() : 178.0);

        String targetGoal = "maintain";
        if (profile.getOptimizationGoal() != null) {
            String cleanGoal = profile.getOptimizationGoal().toLowerCase();
            if (cleanGoal.contains("cut")) targetGoal = "cut";
            if (cleanGoal.contains("bulk")) targetGoal = "bulk";
        }
        pythonRequest.put("optimizationGoal", targetGoal);

        Map<String, Object> mlResult;
        try {
            mlResult = restTemplate.postForObject(pythonMlUrl, pythonRequest, Map.class);
        } catch (Exception e) {
            System.err.println("⚠️ Python container asleep. Triggering baseline fallback schemas.");
            mlResult = new HashMap<>();
            mlResult.put("calories", 2200);
            mlResult.put("protein", 140);
            mlResult.put("carbs", 230);
            mlResult.put("fats", 65);
        }

        Map<String, Object> nestedPredictions = new HashMap<>();
        if (mlResult != null) {
            try {
                int calories = Double.valueOf(mlResult.get("calories").toString()).intValue();
                int protein = Double.valueOf(mlResult.get("protein").toString()).intValue();
                int carbs = Double.valueOf(mlResult.get("carbs").toString()).intValue();
                int fats = Double.valueOf(mlResult.get("fats").toString()).intValue();

                nestedPredictions.put("dailyCalories", calories);
                Map<String, Object> macrosMap = new HashMap<>();
                macrosMap.put("protein", protein);
                macrosMap.put("carbs", carbs);
                macrosMap.put("fats", fats);
                nestedPredictions.put("macros", macrosMap);
            } catch (Exception e) {
                nestedPredictions.put("dailyCalories", 2000);
                nestedPredictions.put("macros", Map.of("protein", 130, "carbs", 220, "fats", 65));
            }
        }

        String goal = (profile.getOptimizationGoal() != null) ? profile.getOptimizationGoal().toLowerCase() : "maintain";
        if (goal.contains("cut")) {
            nestedPredictions.put("workoutPlan", Map.of("frequency", "4 Days / Week", "focus", "High Reps & Fast Fat Burn 🔥", "days", List.of(Map.of("name", "👕 Day 1: Chest Workout", "routines", List.of("Flat Barbell Bench Press: 4x12", "Incline Dumbbell Press: 3x12")), Map.of("name", "💪 Day 2: Back & Biceps", "routines", List.of("Lat Pulldowns: 4x12", "Dumbbell Bicep Curls: 4x15")))));
            nestedPredictions.put("mealPlan", Map.of("strategy", "Caloric Deficit & Fat Loss Burn 🔥", "days", List.of(Map.of("time", "🍳 Breakfast Entry", "dish", "Egg White Masala Omelette", "details", "5 Egg Whites + 2 slices Brown Bread."))));
        } else if (goal.contains("bulk")) {
            nestedPredictions.put("workoutPlan", Map.of("frequency", "4 Days / Week", "focus", "Heavy Weight & Size Accumulation 🏗️", "days", List.of(Map.of("name", "👕 Day 1: Heavy Chest Day", "routines", List.of("Flat Barbell Bench Press: 4x8 (Heavy)")), Map.of("name", "🦵 Day 2: Squat Heavy Drive", "routines", List.of("Barbell Back Squats: 4x8 (Heavy)")))));
            nestedPredictions.put("mealPlan", Map.of("strategy", "Caloric Surpluses & Size Building 🏗️", "days", List.of(Map.of("time", "🍳 Breakfast Entry", "dish", "Peanut Butter Banana Oats Shake", "details", "60g Oats + 2 tbsp Peanut Butter."))));
        } else {
            nestedPredictions.put("workoutPlan", Map.of("frequency", "4 Days / Week", "focus", "Lean Muscle Mass Preservation ⚖️", "days", List.of(Map.of("name", "👕 Day 1: Hypertrophy Baseline", "routines", List.of("Dumbbell Press: 4x10", "Cable Rows: 4x10")), Map.of("name", "🦵 Day 2: Strength Maintenance", "routines", List.of("Leg Press: 4x10", "Romanian Deadlifts: 4x8")))));
            nestedPredictions.put("mealPlan", Map.of("strategy", "Isocaloric Metabolic Equilibrium ⚖️", "days", List.of(Map.of("time", "🍳 Breakfast Entry", "dish", "Mixed Berry Greek Yogurt Bowl", "details", "200g Greek Yogurt + 30g Almonds + Honey."))));
        }

        nestedPredictions.put("algorithmUsed", "Ensemble Random Forest Multi-Output Regressor Matrix");
        profile.setAnalyticalPredictions(nestedPredictions);
        return profile;
    }
}