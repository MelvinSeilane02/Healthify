package com.example.healthify

import android.content.Intent
import android.os.Bundle
import android.widget.*
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
            loadTargets()
        }
    }

    private fun loadTargets() {
        val request = Request.Builder()
            .url("https://exercisedb.p.rapidapi.com/exercises/targetList")
            .get()
            .addHeader("x-rapidapi-key", "3f7e508fadmsh959f9d383b10369p11ec0bjsnf6434848fca6")
            .addHeader("x-rapidapi-host", "exercisedb.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@WorkoutActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                if (json != null) {
                    val targets = JSONArray(json)
                    runOnUiThread {
                        displayTargets(targets)
                    }
                }
            }
        })
    }

    private fun displayTargets(targets: JSONArray) {
        container.removeAllViews()

        for (i in 0 until targets.length()) {
            val target = targets.getString(i)
            val btn = Button(this).apply {
                text = target.replaceFirstChar { it.uppercase() }
                setBackgroundColor(getColor(R.color.primaryBlue))
                setTextColor(getColor(android.R.color.white))
                setOnClickListener {
                    val intent = Intent(this@WorkoutActivity, ExerciseListActivity::class.java)
                    intent.putExtra("target", target)
                    startActivity(intent)
                }
            }
            container.addView(btn)
        }
    }
}
