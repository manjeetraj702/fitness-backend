package com.fittrack.analytics_core.dto;

import lombok.Data;
import java.util.List;

@Data
public class NutritionRequest {
    private List<String> availableIngredients;
    private String dietPreference; // e.g., Vegetarian, Vegan, Keto, High-Protein
}