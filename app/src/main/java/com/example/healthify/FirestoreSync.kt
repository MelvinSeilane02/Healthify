package com.example.healthify

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreSync {

    private val firestore = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    fun syncSettings(language: String, isDark: Boolean, calorieGoal: Int) {
        user?.let {
            val settingsMap = mapOf(
                "language" to language,
                "dark_mode" to isDark,
                "calorie_goal" to calorieGoal
            )

            firestore.collection("users")
                .document(it.uid)
                .set(settingsMap, com.google.firebase.firestore.SetOptions.merge())
        }
    }

    fun saveSetting(userId: String?, key: String, value: Any) {
        val settingsRef = firestore.collection("users").document(userId).collection("settings")
        val settingData = hashMapOf("value" to value)

        settingsRef.document(key).set(settingData)
            .addOnSuccessListener {
                // ✅ Successfully synced to Firestore
            }
            .addOnFailureListener {
                // ❌ Failed to sync
            }
    }
}