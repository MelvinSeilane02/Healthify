package com.example.healthify.models

// ✅ API models — Nutritionix response
data class FoodItem(
    val food_name: String,
    val nf_calories: Double,
    val nf_protein: Double,
    val nf_total_carbohydrate: Double,
    val nf_total_fat: Double
)