package com.example.healthify.mealplanner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.healthify.PrefsManager
import com.example.healthify.R
import com.example.healthify.adapters.MealAdapter
import com.example.healthify.databinding.ActivityMealPlannerBinding
import com.example.healthify.methods.BaseActivity
import com.example.healthify.models.Meal
import com.example.healthify.repository.MealRepository
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.healthify.adapters.MealPagerAdapter

import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MealPlannerActivity : BaseActivity() {

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

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        val adapter = MealPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.breakfast)
                1 -> getString(R.string.lunch)
                else -> getString(R.string.supper)
            }
        }.attach()

        // --- toolbar (uses toolbar defined in toolbar_layout.xml with id @+id/appToolbar) ---
        val toolbar = findViewById<Toolbar>(R.id.appToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.toolbar_title_meal_planner)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Initialize
        mealRepository = MealRepository()
        prefsManager = PrefsManager(this)
        dailyGoal = prefsManager.getCalorieGoal()

        // RecyclerView + Adapter
        // Option A (recommended): adapter accepts a delete callback in constructor
        mealAdapter = MealAdapter(mealList) { meal ->
            handleDeleteMeal(meal)
        }

        // If your MealAdapter instead exposes a mutable property `onDeleteClick`, use:
        // mealAdapter = MealAdapter(mealList)
        // mealAdapter.onDeleteClick = { meal -> handleDeleteMeal(meal) }

        /*binding.recyclerViewMeals.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMeals.adapter = mealAdapter*/

        // Show daily goal (localized)
        binding.tvGoal.text = getString(R.string.daily_goal_label, dailyGoal)

        // Load meals from Firestore and update UI
        loadMeals()

        // Navigate to AddMealActivity
        binding.fabAddMeal.setOnClickListener {
            startActivity(Intent(this, AddMealActivity::class.java))
        }
    }

    private fun handleDeleteMeal(meal: Meal) {
        mealRepository.deleteMeal(meal.id) { success, exception ->
            if (success) {
                Toast.makeText(this, getString(R.string.meal_deleted, meal.name), Toast.LENGTH_SHORT).show()
                loadMeals()
            } else {
                Log.e("MealPlanner", "deleteMeal failed", exception)
                Toast.makeText(this, getString(R.string.meal_delete_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Fetch today's meals
    private fun loadMeals() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, getString(R.string.user_not_logged_in), Toast.LENGTH_SHORT).show()
            return
        }

        mealRepository.getMealsForToday { meals ->
            mealList.clear()
            mealList.addAll(meals)
            mealAdapter.notifyDataSetChanged()

            // Calculate total calories and update UI (localized)
            val totalCalories = meals.sumOf { it.calories.toInt() }
            binding.tvTotalCalories.text = getString(R.string.consumed_label, totalCalories)

            // Update progress bar (0..100)
            val progress = ((totalCalories.toFloat() / dailyGoal) * 100).toInt().coerceAtMost(100)
            binding.progressBar.progress = progress
        }
    }

    override fun onResume() {
        super.onResume()
        loadMeals() // refresh when returning from AddMealActivity
    }
}
