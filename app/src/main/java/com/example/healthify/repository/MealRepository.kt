package com.example.healthify.repository

import com.example.healthify.models.Meal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class MealRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    private val userId = auth.currentUser?.uid

    // ✅ Add Meal to Firestore
    fun addMeal(meal: Meal, onComplete: (Boolean) -> Unit) {
        userId?.let { uid ->
            firestore.collection("users")
                .document(uid)
                .collection("meals")
                .document(meal.id)
                .set(meal)
                .addOnSuccessListener { onComplete(true) }
                .addOnFailureListener { onComplete(false) }
        } ?: onComplete(false)
    }

    // ✅ Fetch Meals for Today
    fun getMealsForToday(onResult: (List<Meal>) -> Unit) {
        userId?.let { uid ->
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
                .addOnFailureListener { onResult(emptyList()) }
        } ?: onResult(emptyList())
    }

    // ✅ Delete Meal by ID
    fun deleteMeal(mealId: String, onComplete: (Boolean) -> Unit) {
        userId?.let { uid ->
            firestore.collection("users")
                .document(uid)
                .collection("meals")
                .document(mealId)
                .delete()
                .addOnSuccessListener { onComplete(true) }
                .addOnFailureListener { onComplete(false) }
        } ?: onComplete(false)
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
