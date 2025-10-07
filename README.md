# 🥗 Healthify – Smart Meal & Fitness Tracker

Healthify is an Android application built to help users manage their **nutrition, exercise, and wellness goals** — combining **meal tracking**, **daily calorie goals**, and **fitness activity monitoring** into one intuitive interface.

---

## 🚀 Features

- 🔐 **Firebase Authentication** – Secure login and registration system.
- ☁️ **Firestore Integration** – Stores user settings, meals, and goals.
- 🍱 **Meal Planner** – Add, view, and manage meals with calorie and macro breakdown.
- 🧠 **Nutritionix API Integration** – Fetch accurate nutrition data from a trusted database.
- 🎯 **Daily Calorie Goal Tracking** – Compare total intake vs. your personalized goal.
- 🏋️ **Exercise & Workout Tracking** – Monitor your physical activity and progress.
- 🌦 **Weather Integration** – Get real-time weather info for outdoor training.
- 🌓 **Theme Switching** – Toggle between light and dark mode.
- 🌍 **Language Preferences** – English, Zulu, and Setswana support.
- 🧩 **Firestore Sync** – Settings automatically synced across devices.
- 🔄 **Offline Storage Ready (Planned)** – Caching data with RoomDB.

---

## ⚙️ Setup & Installation

### 1. Clone the repository
```bash
git clone [https://github.com/MelvinSeilane02/Healthify.git)
```

### 2. Add your own API keys
You’ll need to configure your **Nutritionix** and **OpenWeather** API keys.

Create a local `gradle-wrapper.properties` file inside your project root and add:
```properties
# Local API Keys
NUTRITIONIX_APP_ID=f5d73b4b
NUTRITIONIX_APP_KEY=031e9e6bc34302c050c343c01cea5ea7
OPENWEATHER_API_KEY=your_openweather_api_key_here
```

### 3. Configure build.gradle.kts
In your module’s `build.gradle.kts`, define your API keys:
```kotlin
// Nutritionix API
val nutritionixAppId = "f5d73b4b"
val nutritionixAppKey = "031e9e6bc34302c050c343c01cea5ea7"

// Add to BuildConfig for safe access in code
buildConfigField("String", "NUTRITIONIX_APP_ID", "\"$nutritionixAppId\"")
buildConfigField("String", "NUTRITIONIX_APP_KEY", "\"$nutritionixAppKey\"")
buildConfigField("String", "OPENWEATHER_API_KEY", "\"your_openweather_api_key_here\"")
```

---

## 🍽️ Meal Planning Module

### 🎯 Purpose
Enable users to:
- Search for meals via **Nutritionix API**.
- View nutrition breakdown (calories, macros).
- Track and store meals in **Firestore**.
- Compare consumption to daily calorie goal.

### 🔍 Firestore Structure
```
users/
 └── userId/
      ├── profile/
      ├── settings/
      └── meals/
           ├── 2025-10-06-meal1
           ├── 2025-10-06-meal2
```

### 💡 Example Retrofit Call
```kotlin
@POST("v2/natural/nutrients")
fun getNutritionData(
    @Header("x-app-id") appId: String,
    @Header("x-app-key") apiKey: String,
    @Body request: NutritionRequest
): Call<FoodResponse>
```

### 🧠 MealPlannerActivity
- Displays today’s meals and calories consumed.
- Shows progress bar vs. daily goal.
- Uses RecyclerView to render meals.
- Integrates Firestore for persistence.

---

## 💪 Exercise Feature — Overview

### 🎯 Purpose
The **Exercise module** helps users:
- View their workout or exercise sessions.
- Track total training time vs. goal time.
- View weather data to plan outdoor activities.
- (Planned) Log new workouts or sync with Google Fit.

It merges real-time **OpenWeather API** data with personalized fitness tracking.

---

### ⚙ Main Components

#### 🧩 1. ExerciseActivity.kt & WorkoutActivity.kt
These serve as the **Exercise Dashboard** and **Workout Detail** screens.

**Core Responsibilities**
- Show daily/weekly summaries.
- Fetch and display weather (OpenWeather API).
- Visualize progress toward exercise goals.
- Navigate between screens via buttons or FABs.

**Example Weather Service**
```kotlin
interface WeatherService {
    @GET("data/2.5/weather")
    fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Call<WeatherResponse>
}
```

**Example Usage**
```kotlin
🌤 Pretoria 25° | Clear
```

**Navigation**
```kotlin
fun gotoWorkout(view: View) {
    startActivity(Intent(this, WorkoutActivity::class.java))
    finish()
}
```

---

#### 🌤 2. OpenWeather API Integration

Configured with Retrofit + GsonConverterFactory.

**Base URL:**
```
https://api.openweathermap.org/
```

Stored constants:
```kotlin
private val apiKey = BuildConfig.OPENWEATHER_API_KEY
private val city = "Pretoria"
```

---

#### 🧮 3. Workout Progress Tracking

**Logic Example**
```kotlin
val progressPercent = ((totalMinutes.toFloat() / goalMinutes) * 100)
    .toInt().coerceAtMost(100)
progressCircle.progress = progressPercent
```

Displays:
- Total Training Time  
- Goal Time  
- Circular Progress Indicator  

---

#### 📱 4. UI Files
`activity_exercise.xml` & `activity_workout.xml`

Include:
- ProgressBar (circular)
- TextView (total + goal)
- Button (start new workout)
- Weather text (txtWeather)

Styled with:
- Rounded corners
- Gradients
- Material Components

---

#### 🧠 5. API Prefix Handling
Consistent structure for scalability:
```kotlin
@GET("api/data/2.5/weather")
```

---

#### 🧪 6. Unit Testing Setup
Configured **Mockito + JUnit**:
```kotlin
testImplementation("junit:junit:4.13.2")
testImplementation("org.mockito:mockito-core:5.4.0")
testImplementation("org.mockito:mockito-inline:5.4.0")
```

Includes `LoginRepositoryTest`, with plans for:
- Weather API mocks
- Progress calculations

---

#### 🔐 7. Firebase Integration
While Firebase Auth handles login/register, Exercise modules integrate user identity for:
- Future **exercise session storage**.
- Personalized workout summaries.

---

#### 💾 8. Gradle Configuration
Supports:
- Firebase Auth + Firestore  
- Retrofit, Gson, OkHttp  
- JUnit & Mockito  
- Material Components  
- ViewBinding & DataBinding  
- Secure API key handling via `buildConfigField`

---

### 🧭 How It All Connects
```
LoginActivity → Dashboard → ExerciseActivity → WorkoutActivity
                        ↓
               OpenWeather API
                        ↓
              Progress Tracking (UI + Logic)
```

The **Exercise module** is modular, scalable, and seamlessly integrated with your overall Healthify ecosystem.

---

## 🧑‍💻 Credits

**Developed by:**  
Kamogelo Seilane & Shaun Makhobo  
🎓 *Rosebank College – Software Development Project*

**Technologies Used:**  
- Kotlin  
- Firebase Auth & Firestore  
- Retrofit & Gson  
- Nutritionix & OpenWeather APIs  
- Material Design Components  
- MPAndroidChart (planned)

---
## 📹Youtube Video
```
Healthify Prototype : [https://youtu.be/RipgBzHuHmc)
```

---
## 📚 License
This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

