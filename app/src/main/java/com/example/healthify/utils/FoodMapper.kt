package com.example.healthify.utils

import com.example.healthify.models.Food
import com.example.healthify.models.FoodItem

fun FoodItem.toFood(): Food {
    return Food(
        food_name = this.food_name,
        nf_calories = this.nf_calories,
        nf_protein = this.nf_protein,
        nf_total_carbohydrate = this.nf_total_carbohydrate,
        nf_total_fat = this.nf_total_fat
    )
}