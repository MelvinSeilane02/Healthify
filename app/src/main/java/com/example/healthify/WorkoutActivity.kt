package com.example.healthify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import androidx.core.content.ContextCompat


class WorkoutActivity : AppCompatActivity() {

    private lateinit var btnLoad: MaterialButton
    private lateinit var container: LinearLayout
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        btnLoad = findViewById(R.id.btnSearch)
        container = findViewById(R.id.workoutContainer)

        btnLoad.setOnClickListener {
            container.removeAllViews()
            val muscles = listOf("abs", "chest", "triceps")
            muscles.forEach { loadExercises(it) }
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
                    Toast.makeText(this@WorkoutActivity, "Failed to fetch exercises for $muscle", Toast.LENGTH_SHORT).show()
                    Log.e("WorkoutActivity", "API call failed for $muscle", e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                if (!response.isSuccessful || json.isNullOrEmpty()) {
                    runOnUiThread {
                        Toast.makeText(this@WorkoutActivity, "No exercises found for $muscle", Toast.LENGTH_SHORT).show()
                    }
                    return
                }

                try {
                    val exercises = JSONArray(json)
                    runOnUiThread { displayExercises(muscle, exercises) }
                } catch (e: Exception) {
                    Log.e("WorkoutActivity", "Invalid JSON for $muscle: $json", e)
                    runOnUiThread {
                        Toast.makeText(this@WorkoutActivity, "Invalid response for $muscle", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun displayExercises(muscle: String, exercises: JSONArray) {
        // Add section header
        val header = TextView(this).apply {
            text = muscle.uppercase()
            textSize = 20f
            setTextColor(getColor(android.R.color.black))
            setPadding(8, 24, 8, 12)
        }
        container.addView(header)

        // Create styled exercise cards
        for (i in 0 until exercises.length()) {
            val obj = exercises.getJSONObject(i)
            val name = obj.getString("name")
            val type = obj.optString("type", "N/A")
            val difficulty = obj.optString("difficulty", "N/A")

            val card = MaterialCardView(this).apply {
                radius = 16f
                cardElevation = 6f
                setCardBackgroundColor(getColor(R.color.white))
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 12, 0, 12) }

                val btn = MaterialButton(this@WorkoutActivity).apply {
                    text = "$name\n($difficulty • $type)"
                    setTextColor(getColor(android.R.color.black))
                    setBackgroundColor(getColor(R.color.blue))
                    setOnClickListener {
                        val intent = Intent(this@WorkoutActivity, ExerciseDetailActivity::class.java)
                        intent.putExtra("exercise_name", name)
                        intent.putExtra("type", type)
                        intent.putExtra("difficulty", difficulty)
                        intent.putExtra("instructions", obj.optString("instructions"))
                        startActivity(intent)
                    }
                }

                addView(btn)
            }
            container.addView(card)
        }
    }
}
