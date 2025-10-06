package com.example.healthify

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExerciseDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_detail)

        // Bind UI views
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val nameText = findViewById<TextView>(R.id.exerciseName)
        val typeText = findViewById<TextView>(R.id.exerciseType)
        val difficultyText = findViewById<TextView>(R.id.exerciseDifficulty)
        val instructionText = findViewById<TextView>(R.id.exerciseInstructions)

        // Handle Back button
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Retrieve exercise details from Intent
        val name = intent.getStringExtra("exercise_name") ?: "Unknown Exercise"
        val type = intent.getStringExtra("type")?.ifBlank { "Not specified" } ?: "Not specified"
        val difficulty = intent.getStringExtra("difficulty")?.ifBlank { "N/A" } ?: "N/A"
        val instructions = intent.getStringExtra("instructions")?.ifBlank { "No instructions available." }
            ?: "No instructions available."

        // Display data
        nameText.text = name
        typeText.text = "Type: $type"
        difficultyText.text = "Difficulty: $difficulty"
        instructionText.text = instructions
    }
}
