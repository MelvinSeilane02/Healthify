package com.example.healthify

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ExerciseDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_detail)

        val name = intent.getStringExtra("name")
        val gifUrl = intent.getStringExtra("gifUrl")
        val target = intent.getStringExtra("target")
        val equipment = intent.getStringExtra("equipment")

        val img = findViewById<ImageView>(R.id.imgDetail)
        val txtName = findViewById<TextView>(R.id.txtDetailName)
        val txtTarget = findViewById<TextView>(R.id.txtDetailTarget)
        val txtEquipment = findViewById<TextView>(R.id.txtDetailEquipment)

        txtName.text = name
        txtTarget.text = "Target: $target"
        txtEquipment.text = "Equipment: $equipment"

        Glide.with(this).asGif().load(gifUrl).into(img)
    }
}
