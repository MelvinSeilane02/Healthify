package com.example.healthify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthify.adapters.ExerciseAdapter
import com.example.healthify.api.RetrofitInstance
import com.example.healthify.methods.BaseActivity
import com.example.healthify.models.Exercise
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExerciseListActivity : BaseActivity() {

    private lateinit var rvExercises: RecyclerView
    private lateinit var adapter: ExerciseAdapter
    private lateinit var tvCategoryTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list)

        tvCategoryTitle = findViewById(R.id.tvCategoryTitle)
        rvExercises = findViewById(R.id.rvExercises)
        rvExercises.layoutManager = LinearLayoutManager(this)

        val category = intent.getStringExtra("CATEGORY") ?: "back"
        tvCategoryTitle.text = category.uppercase()

        loadExercises(category)
    }

    private fun loadExercises(bodyPart: String) {
        RetrofitInstance.api.getExercisesByMuscle(bodyPart).enqueue(object : Callback<List<Exercise>> {
            override fun onResponse(call: Call<List<Exercise>>, response: Response<List<Exercise>>) {
                Log.d("ExerciseAPI", "Response: ${response.body()}")

                if (response.isSuccessful) {
                    val exercises = response.body() ?: emptyList()

                    adapter = ExerciseAdapter(exercises) { exercise ->
                        // ✅ open details on click
                        val intent = Intent(this@ExerciseListActivity, ExerciseDetailActivity::class.java)
                        intent.putExtra("name", exercise.name)
                        intent.putExtra("gifUrl", exercise.gifUrl)
                        intent.putExtra("equipment", exercise.equipment)
                        intent.putExtra("target", exercise.target)
                        startActivity(intent)
                    }

                    rvExercises.adapter = adapter
                } else {
                    Toast.makeText(this@ExerciseListActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
                Toast.makeText(this@ExerciseListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("ExerciseListActivity", "API Error", t)
            }
        })
    }
}
