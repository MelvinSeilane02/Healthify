package com.example.healthify

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthify.adapters.ExerciseAdapter
import com.example.healthify.api.ExerciseApiService
import com.example.healthify.methods.BaseActivity
import com.example.healthify.models.Exercise
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class WorkoutActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var exerciseAdapter: ExerciseAdapter
    private val exerciseList = mutableListOf<Exercise>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        recyclerView = findViewById(R.id.recyclerViewExercises)
        recyclerView.layoutManager = LinearLayoutManager(this)
        exerciseAdapter = ExerciseAdapter(exerciseList) { exercise ->
            markWorkoutComplete(exercise)
        }
        recyclerView.adapter = exerciseAdapter

        // Fetch exercises for a specific body part
        fetchExercisesByBodyPart("chest")
    }

    private fun fetchExercisesByBodyPart(bodyPart: String) {
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://exercisedb.p.rapidapi.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ExerciseApiService::class.java)

        val call = api.getExercisesByBodyPart(bodyPart)

        call.enqueue(object : Callback<List<Exercise>> {
            override fun onResponse(
                call: Call<List<Exercise>>,
                response: Response<List<Exercise>>
            ) {
                Log.d("WorkoutActivity", "Response code: ${response.code()} message: ${response.message()}")

                if (response.isSuccessful) {
                    val exercises = response.body() ?: emptyList()
                    if (exercises.isNotEmpty()) {
                        exerciseList.clear()
                        exerciseList.addAll(exercises)
                        exerciseAdapter.notifyDataSetChanged()

                        // ✅ Log first few image URLs
                        exercises.take(3).forEach {
                            Log.d("ExerciseImage", "Image URL: ${it.gifUrl}")
                        }
                    } else {
                        Toast.makeText(
                            this@WorkoutActivity,
                            "No exercises found for $bodyPart",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@WorkoutActivity,
                        "Failed to load exercises: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
                Toast.makeText(
                    this@WorkoutActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    // --- Save Completed Workout ---
    private fun markWorkoutComplete(exercise: Exercise) {
        val workoutData = hashMapOf(
            "userId" to "1234",
            "exerciseName" to exercise.name,
            "duration" to 15,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("user_workouts")
            .add(workoutData)
            .addOnSuccessListener {
                Toast.makeText(this, "Workout saved ✅", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save workout", Toast.LENGTH_SHORT).show()
            }
    }
}
