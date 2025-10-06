package com.example.healthify

import android.util.Log
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
        if (userId == null) return  // do nothing if no user

        val settingsRef = firestore.collection("users")
            .document(userId)
            .collection("settings")

        val settingData = hashMapOf("value" to value)

        settingsRef.document(key).set(settingData)
            .addOnSuccessListener {
                // ✅ Successfully synced to Firestore
                Log.d("FirestoreSync", "Setting '$key' saved: $value")
            }
            .addOnFailureListener { e ->
                // ❌ Failed to sync
                Log.e("FirestoreSync", "Error saving setting '$key'", e)
            }
    }
}