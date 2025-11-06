package com.example.healthify.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.healthify.fragments.BreakfastFragment
import com.example.healthify.fragments.LunchFragment
import com.example.healthify.fragments.SupperFragment
import com.example.healthify.mealplanner.MealPlannerActivity

class MealPagerAdapter(fragment: MealPlannerActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BreakfastFragment()
            1 -> LunchFragment()
            else -> SupperFragment()
        }
    }
}