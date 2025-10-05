package com.example.healthify

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.healthify.databinding.ActivitySettingsBinding
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("HealthifyPrefs", MODE_PRIVATE)

        // Load saved theme
        val darkMode = prefs.getBoolean("darkMode", false)
        binding.switchTheme.isChecked = darkMode
        setThemeMode(darkMode)

        // Load saved calorie goal
        val savedGoal = prefs.getInt("calorieGoal", 0)
        if (savedGoal > 0) binding.etCalorieGoal.setText(savedGoal.toString())

        // Handle Theme Toggle
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("darkMode", isChecked).apply()
            setThemeMode(isChecked)
        }

        // Handle Calorie Goal Save
        binding.btnSaveGoal.setOnClickListener {
            val goalText = binding.etCalorieGoal.text.toString()
            if (goalText.isNotEmpty()) {
                val goal = goalText.toInt()
                prefs.edit().putInt("calorieGoal", goal).apply()
                binding.tvSavedStatus.text = "Goal saved ✅"
            }
        }

        // Handle Language Change
        // Handle Language Change
        binding.languageSpinner.setSelection(getSavedLanguageIndex())
        var isFirstSelection = true

        binding.languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (isFirstSelection) {
                    isFirstSelection = false
                    return
                }

                val langCode = when (position) {
                    1 -> "zu" // Zulu
                    2 -> "xh" // Xhosa
                    else -> "en" // English
                }
                setLocale(langCode)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

    }

    private fun setThemeMode(dark: Boolean) {
        val mode = if (dark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun setLocale(langCode: String) {
        val currentLang = prefs.getString("language", "en")
        if (currentLang == langCode) {
            return // stop loop if the same language is chosen
        }

        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        prefs.edit().putString("language", langCode).apply()

        recreate() // only when language really changes
    }


    private fun getSavedLanguageIndex(): Int {
        return when (prefs.getString("language", "en")) {
            "zu" -> 1 // Zulu
            "tn" -> 2  // Setswana (ISO code is "tn")
            else -> 0 // English
        }
    }
}