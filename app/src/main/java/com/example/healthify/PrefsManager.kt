package com.example.healthify

import android.content.Context

class PrefsManager(context: Context) {

    private val prefs = context.getSharedPreferences("healthify_prefs", Context.MODE_PRIVATE)

    fun saveLanguage(language: String) {
        prefs.edit().putString("language", language).apply()
    }

    fun getLanguage(): String {
        return prefs.getString("language", "en") ?: "en" // Default English
    }

    fun saveTheme(isDarkMode: Boolean) {
        prefs.edit().putBoolean("dark_mode", isDarkMode).apply()
    }

    fun isDarkMode(): Boolean {
        return prefs.getBoolean("dark_mode", false)
    }

    fun saveCalorieGoal(goal: Int) {
        prefs.edit().putInt("calorie_goal", goal).apply()
    }

    fun getCalorieGoal(): Int {
        return prefs.getInt("calorie_goal", 2000) // Default 2000 kcal
    }
}