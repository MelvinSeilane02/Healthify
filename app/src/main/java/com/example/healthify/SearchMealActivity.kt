package com.example.healthify

import android.annotation.SuppressLint
import com.example.healthify.BuildConfig
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.healthify.api.FoodResponse
import com.example.healthify.api.NutritionRequest
import com.example.healthify.api.NutritionixApiService
import com.example.healthify.databinding.ActivitySearchMealBinding
import com.example.healthify.utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchMealActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchMealBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSearch.setOnClickListener {
            val query = binding.etFoodInput.text.toString().trim()
            if (query.isNotEmpty()) {
                searchFood(query)
            }
        }
    }

    private fun searchFood(query: String) {
        val api = RetrofitClient.api
        val request = NutritionRequest(query)

        // ✅ Pass your API keys dynamically
        val call = api.getNutritionData(
            BuildConfig.NUTRITIONIX_APP_ID,
            BuildConfig.NUTRITIONIX_APP_KEY,
            request
        )

        call.enqueue(object : Callback<FoodResponse> {
            @SuppressLint("SetTextI18n")

            override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
                if (response.isSuccessful) {
                    val foodList = response.body()?.foods
                    if (!foodList.isNullOrEmpty()) {
                        val food = foodList[0]
                        binding.tvResult.text = """
                        ${food.food_name}
                        Calories: ${food.nf_calories}
                        Protein: ${food.nf_protein}g
                        Carbs: ${food.nf_total_carbohydrate}g
                        Fat: ${food.nf_total_fat}g
                    """.trimIndent()
                    } else {
                        Toast.makeText(this@SearchMealActivity, "No food data found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMsg = response.errorBody()?.string()
                    Toast.makeText(this@SearchMealActivity, "Error ${response.code()}: $errorMsg", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                Toast.makeText(this@SearchMealActivity, "API Failed: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

}