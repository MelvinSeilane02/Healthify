package com.example.healthify.models

data class Meal(
    val id: String = "",
    val name: String = "",
    val calories: Double = 0.0,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0,
    val category: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)