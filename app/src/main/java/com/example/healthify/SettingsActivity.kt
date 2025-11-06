package com.example.healthify

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatDelegate
import com.example.healthify.databinding.ActivitySettingsBinding
import com.example.healthify.methods.BaseActivity
import com.example.healthify.utils.LocaleHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefsManager: PrefsManager
    private lateinit var firestoreSync: FirestoreSync

    // flag to ignore the spinner's initial selection callback
    private var isFirstSpinnerSelection = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        //LocaleHelper.updateLocale(this)
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init prefs manager and firestore sync
        prefsManager = PrefsManager(this)                // uses "healthify_prefs"
        firestoreSync = FirestoreSync()

        // --- Theme ---
        binding.switchTheme.isChecked = prefsManager.isDarkMode()
        setThemeMode(prefsManager.isDarkMode())
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            prefsManager.saveTheme(isChecked)
            firestoreSync.saveSetting(FirebaseAuth.getInstance().currentUser?.uid, "darkMode", isChecked)
            setThemeMode(isChecked)
        }

        // --- Calorie goal ---
        binding.etCalorieGoal.setText(prefsManager.getCalorieGoal().toString())
        binding.btnSaveGoal.setOnClickListener {
            val goalText = binding.etCalorieGoal.text.toString()
            if (goalText.isNotEmpty()) {
                val goal = goalText.toInt()
                prefsManager.saveCalorieGoal(goal)
                firestoreSync.saveSetting(FirebaseAuth.getInstance().currentUser?.uid, "calorieGoal", goal)
                binding.tvSavedStatus.text = getString(R.string.goal_saved) // use string resource
            }
        }

        // --- Language spinner setup (use saved language) ---
        val savedLang = prefsManager.getLanguage()
        binding.languageSpinner.setSelection(getSavedLanguageIndex(savedLang))

        binding.languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (isFirstSpinnerSelection) {
                    isFirstSpinnerSelection = false
                    return
                }

                val langCode = when (position) {
                    1 -> "tn" // Zulu
                    2 -> "zu" // Setswana
                    else -> "en"
                }

                // only update if actually changed
                if (langCode != prefsManager.getLanguage()) {
                    prefsManager.saveLanguage(langCode)
                    firestoreSync.saveSetting(FirebaseAuth.getInstance().currentUser?.uid, "language", langCode)

                    // apply immediately and restart activity to reload strings
                    LocaleHelper.applyLocale(this@SettingsActivity, langCode)
                    recreate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // --- toolbar (use binding) ---
        val toolbar = binding.includeToolbar.appToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // --- load remote settings (Firestore) and override local if available ---
        loadUserSettings(userId)
    }

    private fun setThemeMode(dark: Boolean) {
        val mode = if (dark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun getSavedLanguageIndex(langCode: String): Int {
        return when (langCode) {
            "zu" -> 1
            "tn" -> 2
            else -> 0
        }
    }

    private fun loadUserSettings(userId: String?) {
        if (userId == null) return

        val settingsRef = FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .collection("settings")

        // Theme
        settingsRef.document("darkMode").get()
            .addOnSuccessListener { doc ->
                val serverValue = doc.getBoolean("value")
                if (serverValue != null) {
                    prefsManager.saveTheme(serverValue)
                    binding.switchTheme.isChecked = serverValue
                    setThemeMode(serverValue)
                }
            }

        // Calorie Goal
        settingsRef.document("calorieGoal").get()
            .addOnSuccessListener { doc ->
                val serverValue = doc.getLong("value")?.toInt()
                if (serverValue != null) {
                    prefsManager.saveCalorieGoal(serverValue)
                    binding.etCalorieGoal.setText(serverValue.toString())
                }
            }

        // Language
        settingsRef.document("language").get()
            .addOnSuccessListener { doc ->
                val serverValue = doc.getString("value")
                if (!serverValue.isNullOrBlank() && serverValue != prefsManager.getLanguage()) {
                    prefsManager.saveLanguage(serverValue)
                    // apply locale immediately
                    LocaleHelper.applyLocale(this@SettingsActivity, serverValue)
                    recreate()
                }
            }
    }

    // apply saved locale early so strings are correct before view inflation on API levels that require it
    override fun attachBaseContext(newBase: Context) {
        val sp = newBase.getSharedPreferences("healthify_prefs", Context.MODE_PRIVATE)
        val langCode = sp.getString("language", "en") ?: "en"
        val context = LocaleHelper.applyLocale(newBase, langCode)
        super.attachBaseContext(context)
    }
}
