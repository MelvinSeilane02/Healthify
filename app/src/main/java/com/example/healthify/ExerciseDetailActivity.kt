package com.example.healthify

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.healthify.R

class ExerciseDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_detail)

        // Get data passed from WorkoutActivity
        val name = intent.getStringExtra("name")
        val gifUrl = intent.getStringExtra("gifUrl")
        val bodyPart = intent.getStringExtra("bodyPart")
        val target = intent.getStringExtra("target")
        val equipment = intent.getStringExtra("equipment")

        // Connect XML views
        val imgExercise = findViewById<ImageView>(R.id.imgExercise)
        val txtExerciseName = findViewById<TextView>(R.id.txtExerciseName)
        val txtBodyPart = findViewById<TextView>(R.id.txtBodyPart)
        val txtTarget = findViewById<TextView>(R.id.txtTarget)
        val txtEquipment = findViewById<TextView>(R.id.txtEquipment)

        // Set text and image
        txtExerciseName.text = name?.replaceFirstChar { it.uppercase() } ?: "Exercise"
        txtBodyPart.text = "Body Part: ${bodyPart ?: "N/A"}"
        txtTarget.text = "Target: ${target ?: "N/A"}"
        txtEquipment.text = "Equipment: ${equipment ?: "N/A"}"

        Glide.with(this)
            .load(gifUrl)
            .into(imgExercise)
    }
}
