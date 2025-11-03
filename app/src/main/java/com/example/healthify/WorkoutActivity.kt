package com.example.healthify

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthify.adapter.ExerciseAdapter
import com.example.healthify.models.Exercise
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class WorkoutActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var exerciseAdapter: ExerciseAdapter
    private val exerciseList = mutableListOf<Exercise>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        recyclerView = findViewById(R.id.recyclerViewExercises)
        recyclerView.layoutManager = LinearLayoutManager(this)
        exerciseAdapter = ExerciseAdapter(exerciseList)
        recyclerView.adapter = exerciseAdapter

        fetchExercisesByMuscle("chest")
    }

    // --- ExerciseDB API ---
    interface ExerciseApi {
        @GET("exercises")
        fun getExercises(@Query("muscle") muscle: String): Call<List<Exercise>>
    }

    private fun fetchExercisesByMuscle(muscle: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://exercisedb.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ExerciseApi::class.java)
        service.getExercises(muscle).enqueue(object : Callback<List<Exercise>> {
            override fun onResponse(call: Call<List<Exercise>>, response: Response<List<Exercise>>) {
                if (response.isSuccessful) {
                    exerciseList.clear()
                    response.body()?.let { exerciseList.addAll(it) }
                    exerciseAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@WorkoutActivity, "Failed to load exercises", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
                Toast.makeText(this@WorkoutActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // --- Mark Workout Complete and Save to Firestore ---
    private fun markWorkoutComplete(exercise: Exercise) {
        val workoutData = hashMapOf(
            "userId" to "1234", // Replace with Firebase Auth UID if available
            "exerciseName" to exercise.name,
            "duration" to 15, // In minutes, or estimate dynamically
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("user_workouts")
            .add(workoutData)
            .addOnSuccessListener {
                Toast.makeText(this, "Workout saved ✅", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK) // So dashboard knows to refresh
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save workout", Toast.LENGTH_SHORT).show()
            }
    }
}
