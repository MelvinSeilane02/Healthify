package com.example.healthify.repository

import com.example.healthify.models.Meal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.Calendar

class MealRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    /**
     * Add Meal to Firestore.
     * onComplete(success, exception) -> exception is null on success.
     */
    fun addMeal(meal: Meal, onComplete: (Boolean, Exception?) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onComplete(false, IllegalStateException("No authenticated user"))
            return
        }

        firestore.collection("users")
            .document(uid)
            .collection("meals")
            .document(meal.id)
            .set(meal, SetOptions.merge()) // merge to be safe; change to .set(meal) to overwrite
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e) }
    }

    /**
     * Fetch meals for today (timestamp >= start of day)
     */
    fun getMealsForToday(onResult: (List<Meal>) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onResult(emptyList())
            return
        }

        val todayStart = getStartOfDay()
        firestore.collection("users")
            .document(uid)
            .collection("meals")
            .whereGreaterThanOrEqualTo("timestamp", todayStart)
            .get()
            .addOnSuccessListener { snapshot ->
                val meals = snapshot.toObjects(Meal::class.java)
                onResult(meals)
            }
            .addOnFailureListener { _ -> onResult(emptyList()) }
    }

    /**
     * Delete meal by ID
     */
    fun deleteMeal(mealId: String, onComplete: (Boolean, Exception?) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onComplete(false, IllegalStateException("No authenticated user"))
            return
        }

        firestore.collection("users")
            .document(uid)
            .collection("meals")
            .document(mealId)
            .delete()
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e) }
    }

    private fun getStartOfDay(): Long {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }
}
