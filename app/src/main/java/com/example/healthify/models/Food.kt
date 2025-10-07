package com.example.healthify.models

// ✅ API models — Nutritionix response

data class Food(
    val food_name: String = "",
    val nf_calories: Double = 0.0,
    val nf_protein: Double = 0.0,
    val nf_total_carbohydrate: Double = 0.0,
    val nf_total_fat: Double = 0.0,
    val photo: Photo? = null
)

data class Photo(
    val thumb: String? = null
)