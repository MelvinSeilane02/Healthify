package com.example.healthify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class WorkoutActivity : AppCompatActivity() {

    private lateinit var btnLoad: Button
    private lateinit var container: LinearLayout
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        btnLoad = findViewById(R.id.btnSearch)
        container = findViewById(R.id.workoutContainer)

        btnLoad.setOnClickListener {
            loadExercises("abs")
            loadExercises("chest")
            loadExercises("triceps")

        }
    }

    private fun loadExercises(muscle: String) {
        val url = "https://exercises-by-api-ninjas.p.rapidapi.com/v1/exercises?muscle=$muscle"

        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("x-rapidapi-key", "3f7e508fadmsh959f9d383b10369p11ec0bjsnf6434848fca6")
            .addHeader("x-rapidapi-host", "exercises-by-api-ninjas.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@WorkoutActivity, "Failed to fetch exercises", Toast.LENGTH_SHORT).show()
                    Log.e("WorkoutActivity", "API call failed", e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                if (!response.isSuccessful || json.isNullOrEmpty()) {
                    runOnUiThread {
                        Toast.makeText(this@WorkoutActivity, "No exercises found", Toast.LENGTH_SHORT).show()
                    }
                    return
                }

                try {
                    val exercises = JSONArray(json)
                    runOnUiThread {
                        displayExercises(exercises)
                    }
                } catch (e: Exception) {
                    Log.e("WorkoutActivity", "Invalid JSON format: $json", e)
                    runOnUiThread {
                        Toast.makeText(this@WorkoutActivity, "Invalid response format", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun displayExercises(exercises: JSONArray) {
        container.removeAllViews()

        for (i in 0 until exercises.length()) {
            val obj = exercises.getJSONObject(i)
            val name = obj.getString("name")
            val type = obj.optString("type")
            val difficulty = obj.optString("difficulty")

            val btn = Button(this).apply {
                text = "$name\n($difficulty, $type)"
                setBackgroundColor(getColor(R.color.primaryBlue))
                setTextColor(getColor(android.R.color.white))
                setOnClickListener {
                    val intent = Intent(this@WorkoutActivity, ExerciseDetailActivity::class.java)
                    intent.putExtra("exercise_name", name)
                    intent.putExtra("type", type)
                    intent.putExtra("difficulty", difficulty)
                    intent.putExtra("instructions", obj.optString("instructions"))
                    startActivity(intent)
                }
            }
            container.addView(btn)
        }
    }
}
