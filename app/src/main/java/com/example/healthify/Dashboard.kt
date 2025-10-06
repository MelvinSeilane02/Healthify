package com.example.healthify

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.*
import androidx.activity.enableEdgeToEdge
import com.example.healthify.WorkoutActivity
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class Dashboard : AppCompatActivity() {

    private lateinit var txtWeather: TextView
    private lateinit var progressCircle: ProgressBar
    private lateinit var txtTotalTraining: TextView
    private lateinit var txtGoal: TextView
    private lateinit var btnMealLogging: Button

    // Replace this with your real OpenWeather API key
    private val apiKey = "YOUR_OPENWEATHER_API_KEY"
    private val city = "Pretoria"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        // Handle window insets (status bar padding)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Bind UI elements
        txtWeather = findViewById(R.id.txtWeather)
        progressCircle = findViewById(R.id.progressCircle)
        txtTotalTraining = findViewById(R.id.txtTotalTraining)
        txtGoal = findViewById(R.id.txtGoal)
        btnMealLogging = findViewById(R.id.btnMealLogging)

        // Fetch live weather
        fetchWeather(city)

        // Update training progress
        updateTrainingProgress(totalMinutes = 180, goalMinutes = 135)

        // Navigate to Meal Logging
        btnMealLogging.setOnClickListener {
           // startActivity(Intent(this, MealLoggingActivity::class.java))
        }
    }

    /** --- WEATHER API SETUP --- **/
    interface WeatherService {
        @GET("data/2.5/weather")
        fun getWeather(
            @Query("q") city: String,
            @Query("appid") apiKey: String,
            @Query("units") units: String = "metric"
        ): Call<WeatherResponse>
    }

    data class WeatherResponse(
        val weather: List<Weather>,
        val main: Main
    )
    data class Weather(val main: String)
    data class Main(val temp: Float)

    private fun fetchWeather(city: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherService::class.java)
        val call = service.getWeather(city, apiKey)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    val temp = data?.main?.temp?.toInt() ?: 0
                    val condition = data?.weather?.firstOrNull()?.main ?: "Clear"
                    txtWeather.text = "🌤️ $city  ${temp}° | $condition"
                } else {
                    txtWeather.text = "⚠️ Weather unavailable"
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                txtWeather.text = "⚠️ Network error"
            }
        })
    }

    /** --- TRAINING PROGRESS --- **/
    private fun updateTrainingProgress(totalMinutes: Int, goalMinutes: Int) {
        txtTotalTraining.text = "Total Training: ${totalMinutes / 60}:${totalMinutes % 60}h"
        txtGoal.text = "Goal: ${goalMinutes / 60}:${goalMinutes % 60}h"

        val progressPercent = ((totalMinutes.toFloat() / goalMinutes) * 100)
            .toInt().coerceAtMost(100)
        progressCircle.progress = progressPercent
    }

    fun gotoWorkout(view: View) {
        startActivity(Intent(this, WorkoutActivity::class.java))
        finish()
    }
}
