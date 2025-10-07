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
import com.example.healthify.mealplanner.AddMealActivity
import com.example.healthify.mealplanner.MealPlannerActivity
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class Dashboard : AppCompatActivity() {

    private lateinit var txtWeather: TextView
    private lateinit var progressCircle: ProgressBar
    private lateinit var txtTotalTraining: TextView
    private lateinit var txtGoal: TextView
    private lateinit var btnMealLogging: Button

    // --- API Keys and Config ---
    private val weatherApiKey = "YOUR_OPENWEATHER_API_KEY"
    private val city = "Pretoria"

    // Replace this with your real backend API if you have one
    private val trainingApiBaseUrl = "https://66f7dcd3a1b1f9e6274f.mockapi.io/api/v1/"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtWeather = findViewById(R.id.txtWeather)
        progressCircle = findViewById(R.id.progressCircle)
        txtTotalTraining = findViewById(R.id.txtTotalTraining)
        txtGoal = findViewById(R.id.txtGoal)
       // btnMealLogging = findViewById(R.id.btnMealLogging)

        // Fetch live weather data
        fetchWeather(city)

        // Fetch training data from an API
        fetchTrainingProgress()

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
        val call = service.getWeather(city, weatherApiKey)

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

    /** --- TRAINING PROGRESS API --- **/
    interface TrainingService {
        @GET("training/progress")
        fun getTrainingProgress(
            @Query("user_id") userId: String = "1234"
        ): Call<TrainingResponse>
    }

    data class TrainingResponse(
        val totalMinutes: Int,
        val goalMinutes: Int
    )

    private fun fetchTrainingProgress() {
        val retrofit = Retrofit.Builder()
            .baseUrl(trainingApiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(TrainingService::class.java)
        val call = service.getTrainingProgress()

        call.enqueue(object : Callback<TrainingResponse> {
            override fun onResponse(call: Call<TrainingResponse>, response: Response<TrainingResponse>) {
                if (response.isSuccessful) {
                    val progress = response.body()
                    if (progress != null) {
                        updateTrainingProgress(progress.totalMinutes, progress.goalMinutes)
                    }
                } else {
                    txtTotalTraining.text = "⚠️ Progress unavailable"
                }
            }

            override fun onFailure(call: Call<TrainingResponse>, t: Throwable) {
                txtTotalTraining.text = "⚠️ Network error"
            }
        })
    }

    /** --- TRAINING PROGRESS DISPLAY --- **/
    private fun updateTrainingProgress(totalMinutes: Int, goalMinutes: Int) {
        txtTotalTraining.text = "Total Training: ${totalMinutes / 60}h ${totalMinutes % 60}m"
        txtGoal.text = "Goal: ${goalMinutes / 60}h ${goalMinutes % 60}m"

        val progressPercent = ((totalMinutes.toFloat() / goalMinutes) * 100)
            .toInt().coerceAtMost(100)
        progressCircle.progress = progressPercent
    }

    fun gotoWorkout(view: View) {
        startActivity(Intent(this, WorkoutActivity::class.java))
       // finish()
    }

    fun gotoSettings(view: View) {
        startActivity(Intent(this, SettingsActivity::class.java))
        finish()
    }

    fun gotoMealPlan(view: View) {
        startActivity(Intent(this, MealPlannerActivity::class.java))
        //finish()
    }
}
