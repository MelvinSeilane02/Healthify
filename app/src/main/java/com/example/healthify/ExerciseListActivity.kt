package com.example.healthify

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthify.adapters.ExerciseAdapter
import com.example.healthify.api.RetrofitInstance
import com.example.healthify.models.Exercise
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExerciseListActivity : AppCompatActivity() {

    private lateinit var rvExercises: RecyclerView
    private lateinit var adapter: ExerciseAdapter
    private lateinit var tvCategoryTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list)

        tvCategoryTitle = findViewById(R.id.tvCategoryTitle)
        rvExercises = findViewById(R.id.rvExercises)
        rvExercises.layoutManager = LinearLayoutManager(this)

        val category = intent.getStringExtra("CATEGORY") ?: "back"
        tvCategoryTitle.text = category.uppercase()

        loadExercises(category)
    }

    private fun loadExercises(muscle: String) {
        val call = RetrofitInstance.api.getExercisesByMuscle(muscle)

        call.enqueue(object : Callback<List<Exercise>> {
            override fun onResponse(
                call: Call<List<Exercise>>,
                response: Response<List<Exercise>>
            ) {
                if (response.isSuccessful) {
                    val exercises = response.body() ?: emptyList()
                    adapter = ExerciseAdapter(exercises)
                    rvExercises.adapter = adapter
                } else {
                    Toast.makeText(this@ExerciseListActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
                Toast.makeText(this@ExerciseListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("ExerciseListActivity", "API Error", t)
            }
        })
    }

    private fun markExerciseAsCompleted(exercise: Exercise) {
        Toast.makeText(this, "${exercise.name} marked as completed!", Toast.LENGTH_SHORT).show()

        // Optional Firestore save
        val db = Firebase.firestore
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val workoutData = mapOf(
            "name" to exercise.name,
            "muscle" to exercise.bodyPart,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("user_workouts")
            .document(userId)
            .collection("completed_exercises")
            .add(workoutData)
            .addOnSuccessListener {
                Log.d("Firestore", "✅ Saved: ${exercise.name}")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "❌ Failed to save exercise", e)
            }
    }
}
