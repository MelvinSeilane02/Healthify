package com.example.healthify

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.healthify.databinding.ActivitySettingsBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var firestoreSync: FirestoreSync

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("HealthifyPrefs", MODE_PRIVATE)
        firestoreSync = FirestoreSync() // initialize properly

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
            firestoreSync.saveSetting(FirebaseAuth.getInstance().currentUser?.uid ,"darkMode", isChecked) // sync
            setThemeMode(isChecked)
        }

        // Handle Calorie Goal Save
        binding.btnSaveGoal.setOnClickListener {
            val goalText = binding.etCalorieGoal.text.toString()
            if (goalText.isNotEmpty()) {
                val goal = goalText.toInt()
                prefs.edit().putInt("calorieGoal", goal).apply()
                firestoreSync.saveSetting(FirebaseAuth.getInstance().currentUser?.uid ,"calorieGoal", goal) // sync
                binding.tvSavedStatus.text = "Goal saved ✅"
            }
        }

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
                    2 -> "tn" // Setswana
                    else -> "en" // English
                }
                setLocale(langCode)
                firestoreSync.saveSetting(FirebaseAuth.getInstance().currentUser?.uid,"language", langCode) // sync
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

        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)

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