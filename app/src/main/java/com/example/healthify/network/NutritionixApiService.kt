package com.example.healthify.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


// Request models
data class NutritionRequest(
    val query: String // e.g. "1 cup rice"
)

// Response models (simplified)
data class FoodResponse(
    val foods: List<FoodItem>
)

data class FoodItem(
    val food_name: String,
    val nf_calories: Float,
    val nf_protein: Float,
    val nf_total_carbohydrate: Float,
    val nf_total_fat: Float
)

interface NutritionixApiService {

    @POST("v2/natural/nutrients")
    fun getNutritionData(
        @Header("x-app-id") appId: String,
        @Header("x-app-key") appKey: String,
        @Body request: NutritionRequest
    ): Call<FoodResponse>
}
