package com.example.healthify.mealplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthify.PrefsManager
import com.example.healthify.R
import com.example.healthify.adapters.MealAdapter
import com.example.healthify.databinding.ActivityMealPlannerBinding
import com.example.healthify.models.Meal
import com.example.healthify.repository.MealRepository
import com.google.firebase.auth.FirebaseAuth

class MealPlannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealPlannerBinding
    private lateinit var prefsManager: PrefsManager
    private lateinit var mealRepository: MealRepository
    private lateinit var mealAdapter: MealAdapter
    private val mealList = mutableListOf<Meal>()
    private var dailyGoal = 2000 // default fallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealPlannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ Initialize components
        mealRepository = MealRepository()
        prefsManager = PrefsManager(this)
        dailyGoal = prefsManager.getCalorieGoal()

        // ✅ Setup RecyclerView and Adapter
        mealAdapter = MealAdapter(mealList)
        binding.recyclerViewMeals.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMeals.adapter = mealAdapter

        // ✅ Set delete listener
        mealAdapter.onDeleteClick = { meal ->
            mealRepository.deleteMeal(meal.id) { success ->
                if (success) {
                    Toast.makeText(this, "${meal.name} deleted", Toast.LENGTH_SHORT).show()
                    loadMeals() // refresh list
                } else {
                    Toast.makeText(this, "Failed to delete meal", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // ✅ Show daily goal
        binding.tvGoal.text = "Daily Goal: $dailyGoal kcal"

        // ✅ Load meals from Firestore
        loadMeals()

        // ✅ FAB: Navigate to AddMealActivity
        binding.fabAddMeal.setOnClickListener {
            startActivity(Intent(this, AddMealActivity::class.java))
        }
        setSupportActionBar(findViewById(R.id.appToolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.appToolbar)
            .setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    // ✅ Fetch today's meals
    private fun loadMeals() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        mealRepository.getMealsForToday { meals ->
            mealList.clear()
            mealList.addAll(meals)
            mealAdapter.notifyDataSetChanged()

            // ✅ Calculate total calories
            val totalCalories = meals.sumOf { it.calories.toInt() }
            binding.tvTotalCalories.text = "Consumed: $totalCalories kcal"

            // ✅ Update progress bar
            val progress = ((totalCalories.toFloat() / dailyGoal) * 100)
                .toInt()
                .coerceAtMost(100)
            binding.progressBar.progress = progress
        }
    }

    override fun onResume() {
        super.onResume()
        loadMeals() // refresh when returning from AddMealActivity
    }


}
