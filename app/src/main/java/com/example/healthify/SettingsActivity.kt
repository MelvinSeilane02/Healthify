package com.example.healthify

import android.content.Context
import com.example.healthify.PrefsManager
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.healthify.databinding.ActivitySettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var prefsManager: PrefsManager

    private lateinit var firestoreSync: FirestoreSync

    override fun onCreate(savedInstanceState: Bundle?) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("HealthifyPrefs", MODE_PRIVATE)
        prefsManager = PrefsManager(this)
        firestoreSync = FirestoreSync() // initialize properly

        // Load saved theme
        val darkMode = prefs.getBoolean("darkMode", false)
        binding.switchTheme.isChecked = darkMode
        setThemeMode(darkMode)

        // Load saved calorie goal
        val savedGoal = prefs.getInt("calorieGoal", 0)
        if (savedGoal > 0) binding.etCalorieGoal.setText(savedGoal.toString())

        // 🔥 Sync from Firestore (if logged in)
        loadUserSettings(userId)


        // Handle Theme Toggle
        binding.switchTheme.isChecked = prefsManager.isDarkMode()
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            prefsManager.saveTheme(isChecked)
            firestoreSync.saveSetting(FirebaseAuth.getInstance().currentUser?.uid, "darkMode", isChecked)
            setThemeMode(isChecked)
        }

        // Handle Calorie Goal Save
        binding.etCalorieGoal.setText(prefsManager.getCalorieGoal().toString())
        binding.btnSaveGoal.setOnClickListener {
            val goalText = binding.etCalorieGoal.text.toString()
            if (goalText.isNotEmpty()) {
                val goal = goalText.toInt()
                prefsManager.saveCalorieGoal(goal)
                firestoreSync.saveSetting(FirebaseAuth.getInstance().currentUser?.uid, "calorieGoal", goal)
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

        setSupportActionBar(findViewById(R.id.appToolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.appToolbar)
            .setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }


    }

    private fun setThemeMode(dark: Boolean) {
        val mode = if (dark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun setLocale(langCode: String) {
        val currentLang = prefsManager.getLanguage()
        if (currentLang == langCode) return

        prefsManager.saveLanguage(langCode)
        recreate()
    }


    private fun getSavedLanguageIndex(): Int {
        return when (prefs.getString("language", "en")) {
            "zu" -> 1 // Zulu
            "tn" -> 2  // Setswana (ISO code is "tn")
            else -> 0 // English
        }
    }

    private fun loadUserSettings(userId: String?){
        if (userId != null) {
            val settingsRef = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("settings")

            // Theme
            settingsRef.document("darkMode").get()
                .addOnSuccessListener { doc ->
                    val serverValue = doc.getBoolean("value")
                    if (serverValue != null) {
                        binding.switchTheme.isChecked = serverValue
                        setThemeMode(serverValue)

                        // also update local storage
                        prefs.edit().putBoolean("darkMode", serverValue).apply()
                    }
                }

            // Calorie Goal
            settingsRef.document("calorieGoal").get()
                .addOnSuccessListener { doc ->
                    val serverValue = doc.getLong("value")?.toInt()
                    if (serverValue != null) {
                        binding.etCalorieGoal.setText(serverValue.toString())

                        prefs.edit().putInt("calorieGoal", serverValue).apply()
                    }
                }

            // Language
            settingsRef.document("language").get()
                .addOnSuccessListener { doc ->
                    val serverValue = doc.getString("value")
                    if (serverValue != null) {
                        prefs.edit().putString("language", serverValue).apply()
                        setLocale(serverValue)
                    }
                }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("HealthifyPrefs", Context.MODE_PRIVATE)
        val langCode = prefs.getString("language", "en") ?: "en"
        val context = com.example.healthify.utils.LocaleHelper.applyLocale(newBase, langCode)
        super.attachBaseContext(context)
    }
}