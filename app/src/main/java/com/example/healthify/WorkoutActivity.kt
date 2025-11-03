package com.example.healthify

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthify.adapters.ExerciseAdapter
import com.example.healthify.models.Exercise
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.OkHttpClient
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

        // You can change this to any muscle name, e.g. "biceps", "legs"
        fetchExercisesByMuscle("chest")
    }

    // --- ✅ Correct ExerciseDB API Interface ---
    interface ExerciseApi {
        @GET("exercises")
        fun getExercises(
            @Query("muscle") muscle: String
        ): Call<List<Exercise>>
    }

    // --- ✅ Updated Function with Headers ---
    private fun fetchExercisesByMuscle(muscle: String) {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("x-rapidapi-key", "3f7e508fadmsh959f9d383b10369p11ec0bjsnf6434848fca6")
                    .addHeader("x-rapidapi-host", "exercisedb.p.rapidapi.com")
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://exercisedb.p.rapidapi.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ExerciseApi::class.java)

        api.getExercises(muscle).enqueue(object : Callback<List<Exercise>> {
            override fun onResponse(call: Call<List<Exercise>>, response: Response<List<Exercise>>) {
                Log.d("WorkoutActivity", "Response code: ${response.code()} message: ${response.message()}")
                if (response.isSuccessful) {
                    val exercises = response.body() ?: emptyList()
                    if (exercises.isNotEmpty()) {
                        exerciseList.clear()
                        exerciseList.addAll(exercises)
                        exerciseAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@WorkoutActivity, "No exercises found for $muscle", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@WorkoutActivity, "Failed to load exercises: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
                Toast.makeText(this@WorkoutActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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
