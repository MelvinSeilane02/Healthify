package com.example.healthify

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.healthify.R

class ExerciseDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_detail)

        // Get UI references
        val nameText = findViewById<TextView>(R.id.exerciseName)
        val typeText = findViewById<TextView>(R.id.exerciseType)
        val difficultyText = findViewById<TextView>(R.id.exerciseDifficulty)
        val instructionText = findViewById<TextView>(R.id.exerciseInstructions)

        // Get passed data from Intent
        val name = intent.getStringExtra("exercise_name") ?: "Unknown Exercise"
        val type = intent.getStringExtra("type") ?: "N/A"
        val difficulty = intent.getStringExtra("difficulty") ?: "N/A"
        val instructions = intent.getStringExtra("instructions") ?: "No instructions available."

        // Display the data
        nameText.text = name
        typeText.text = "Type: $type"
        difficultyText.text = "Difficulty: $difficulty"
        instructionText.text = instructions
    }
}
