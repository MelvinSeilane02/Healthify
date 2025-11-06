package com.example.healthify

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.healthify.methods.BaseActivity
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException

class ExerciseListActivity : BaseActivity() {

    private lateinit var container: LinearLayout
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply saved language at app start
        /*val prefs = PrefsManager(applicationContext)
        val lang = prefs.getLanguage()
        LocaleHelper.applyLocale(applicationContext, lang)*/

        setContentView(R.layout.activity_exercise_list)

        container = findViewById(R.id.exerciseContainer)
        val target = intent.getStringExtra("target")
        Log.d("ExerciseListActivity", "Received target: $target")
        if (target != null) {
            loadExercises(target)
        }


    }

    private fun loadExercises(target: String) {
        val request = Request.Builder()
            .url("https://exercisedb.p.rapidapi.com/exercises/target/%7Btarget%7D")
            .get()
            .addHeader("x-rapidapi-key", "3f7e508fadmsh959f9d383b10369p11ec0bjsnf6434848fca6")
            .addHeader("x-rapidapi-host", "exercisedb.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ExerciseListActivity, "Failed to load exercises", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                if (json != null) {
                    if (json.trim().startsWith("{")) {
                        // This means it's an error object, not an array
                        Log.e("ExerciseListActivity", "Error response: $json")
                        runOnUiThread {
                            Toast.makeText(this@ExerciseListActivity, "Invalid target or API error", Toast.LENGTH_SHORT).show()
                        }
                        return
                    }

                    val exercises = JSONArray(json)
                    runOnUiThread {
                        displayExercises(exercises)
                    }
                }
            }
        })
    }

    private fun displayExercises(exercises: JSONArray) {
        container.removeAllViews()
        for (i in 0 until exercises.length()) {
            val item = exercises.getJSONObject(i)
            val name = item.getString("name")
            val equipment = item.getString("equipment")
            val gifUrl = item.getString("gifUrl")

            val layout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(16, 16, 16, 16)
            }

            val image = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    400
                )
                Picasso.get().load(gifUrl).into(this)
            }

            val text = TextView(this).apply {
                text = "$name\nEquipment: $equipment"
                textSize = 16f
            }

            layout.addView(image)
            layout.addView(text)
            container.addView(layout)
        }
    }
}
