package com.example.healthify.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthify.R
import com.example.healthify.adapters.MealAdapter
import com.example.healthify.databinding.FragmentMealListBinding
import com.example.healthify.models.Meal
import com.example.healthify.repository.MealRepository
import com.google.firebase.auth.FirebaseAuth
import kotlin.text.equals

class SupperFragment : Fragment() {

    private lateinit var binding: FragmentMealListBinding
    private lateinit var mealAdapter: MealAdapter
    private val mealRepository = MealRepository()
    private val mealList = mutableListOf<Meal>()
    private val dailyGoal = 2000 // example, can be fetched dynamically

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealListBinding.inflate(inflater, container, false)
        setupRecyclerView()
        loadMeals()
        return binding.root
    }

    private fun setupRecyclerView() {
        mealAdapter = MealAdapter(mealList, onDeleteClick = { meal -> handleDeleteMeal(meal) })
        binding.recyclerViewMeals.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMeals.adapter = mealAdapter
    }

    private fun handleDeleteMeal(meal: Meal) {
        mealRepository.deleteMeal(meal.id) { success, exception ->
            if (success) {
                Toast.makeText(requireContext(), getString(R.string.meal_deleted, meal.name), Toast.LENGTH_SHORT).show()
                loadMeals()
            } else {
                Log.e("MealPlanner", "deleteMeal failed", exception)
                Toast.makeText(requireContext(), getString(R.string.meal_delete_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadMeals() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), getString(R.string.user_not_logged_in), Toast.LENGTH_SHORT).show()
            return
        }

        // ✅ Filter by meal type
        mealRepository.getMealsForToday { meals ->
            val filtered = meals.filter { it.category.equals("Supper", true) }
            mealList.clear()
            mealList.addAll(filtered)
            mealAdapter.notifyDataSetChanged()

            // Update summary text and progress if needed
            val totalCalories = filtered.sumOf { it.calories.toInt() }
            binding.tvTotalCalories.text = getString(R.string.consumed_label, totalCalories)

            val progress = ((totalCalories.toFloat() / dailyGoal) * 100).toInt().coerceAtMost(100)
            binding.progressBar.progress = progress
        }
    }

    override fun onResume() {
        super.onResume()
        loadMeals()
    }
}