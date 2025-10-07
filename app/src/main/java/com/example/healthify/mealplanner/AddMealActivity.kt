package com.example.healthify.mealplanner

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.healthify.BuildConfig
import com.example.healthify.R
import com.example.healthify.repository.MealRepository
import com.example.healthify.databinding.ActivityAddMealBinding
import com.example.healthify.models.Food
import com.example.healthify.models.Meal
import com.example.healthify.network.FoodResponse
import com.example.healthify.network.NutritionRequest
import com.example.healthify.network.RetrofitClient
import com.example.healthify.utils.toFood
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddMealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMealBinding
    private lateinit var mealRepository: MealRepository
    private var currentFood: Food? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //binding = ActivitySearchMealBinding.inflate(layoutInflater)
        binding = ActivityAddMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mealRepository = MealRepository()

        val selectedCategory = binding.spinnerMealCategory.selectedItem.toString()

        binding.btnSearch.setOnClickListener {
            val query = binding.etFoodInput.text.toString().trim()
            if (query.isNotEmpty()) {
                searchFood(query)
            }
        }

        // Add meal to Firestore
        binding.btnAddMeal.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid


            if (userId != null) {
                // User is signed in
                //val userId
                Log.d(TAG, "User is signed in. UID: $userId")
                // Proceed with logic for a signed-in user
                // For example: loadUserProfile(userId)
            } else {
                // No user is signed in
                Log.d(TAG, "No user is currently signed in.")
                // Proceed with logic for a signed-out user
                // For example: redirectToLoginActivity()
            }

            val food = currentFood
            if (userId != null && food != null) {
                val meal = Meal(
                    id = System.currentTimeMillis().toString(),
                    name = food.food_name,
                    calories = food.nf_calories,
                    protein = food.nf_protein,
                    carbs = food.nf_total_carbohydrate,
                    fat = food.nf_total_fat,
                    category = selectedCategory // ✅ store category
                )

                mealRepository.addMeal(meal) { success ->
                    if (success) {
                        Toast.makeText(this, "${meal.name} added!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to save meal!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {

                Log.d(TAG, "No user is currently signed in.")
                Log.d(TAG, "No user is currently signed in.")
                Log.d(TAG, "No user is currently signed in.")
                Log.d(TAG, "Fat : ")

                Toast.makeText(this, "No meal repository to save!", Toast.LENGTH_SHORT).show()
            }
        }

        setSupportActionBar(findViewById(R.id.appToolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.appToolbar)
            .setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

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
                        // Convert and store for later saving
                        currentFood = food.toFood()

                        binding.tvResult.text = """
                        ${food.food_name}
                        Calories: ${food.nf_calories}
                        Protein: ${food.nf_protein}g
                        Carbs: ${food.nf_total_carbohydrate}g
                        Fat: ${food.nf_total_fat}g
                    """.trimIndent()



                        // ✅ Show Add Meal button
                        binding.btnAddMeal.visibility = View.VISIBLE

                    } else {
                        Toast.makeText(this@AddMealActivity, "No food repository found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMsg = response.errorBody()?.string()
                    Toast.makeText(this@AddMealActivity, "Error ${response.code()}: $errorMsg", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                Toast.makeText(this@AddMealActivity, "API Failed: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    /*private fun toFood(foodItem: FoodItem): Food {
        return Food(
            food_name = foodItem.food_name,
            nf_calories = foodItem.nf_calories,
            nf_protein = foodItem.nf_protein,
            nf_total_carbohydrate = foodItem.nf_total_carbohydrate,
            nf_total_fat = foodItem.nf_total_fat
        )
    }*/

}