// com/example/healthify/utils/FoodMapper.kt

package com.example.healthify.utils

import com.example.healthify.models.Food
import com.example.healthify.network.FoodItem as NetworkFoodItem  // ✅ alias to avoid confusion

// Extension function for the *network* FoodItem
fun NetworkFoodItem.toFood(): Food {
    return Food(
        food_name = this.food_name,
        nf_calories = this.nf_calories.toDouble(),
        nf_protein = this.nf_protein.toDouble(),
        nf_total_carbohydrate = this.nf_total_carbohydrate.toDouble(),
        nf_total_fat = this.nf_total_fat.toDouble()
    )
}
