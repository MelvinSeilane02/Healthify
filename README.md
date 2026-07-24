#  Healthify – Smart Meal & Fitness Tracker

Healthify is an Android application developed as a collaborative Software Development project by Thabiso Shaun Charles Makhobo and Kamogelo Melvin Seilane. The application was created to address two important aspects of healthy living that are often overlooked when separated—nutrition management and physical wellness.

Many people struggle to maintain healthy lifestyles because they do not have a simple way to monitor both their eating habits and fitness activities in one place. Healthify was designed to solve this problem by combining meal planning, calorie tracking, exercise monitoring, and personalized wellness features into a single mobile application.

Rather than simply counting calories or recording workouts, Healthify provides users with meaningful insights that encourage healthier decision-making and long-term wellness habits.

---

##  Why This Project Exists

Healthify was developed to help users:

* Monitor their daily calorie intake.
* Plan and track meals using accurate nutritional information.
* Track exercise sessions and fitness goals.
* View weather conditions before outdoor training activities.
* Personalize their experience through theme and language preferences.
* Improve their overall health by making informed lifestyle choices.

The project demonstrates the practical application of Android development, API integration, cloud databases, authentication systems, and collaborative software engineering practices.

---

##  Collaboration & Teamwork

Healthify was developed collaboratively by:

* **Thabiso Shaun Charles Makhobo**
* **Kamogelo Melvin Seilane**

Throughout the project, we worked closely together to design, develop, test, and document the application while maintaining a modular development approach.

### Project Responsibilities

#### Thabiso Shaun Charles Makhobo

Responsible for:

* Exercise and Workout Tracking Module.
* OpenWeather API integration.
* Exercise progress calculations and visualizations.
* Weather-based fitness functionality.
* Exercise user interface implementation.
* Fitness goal tracking logic.
* Firebase integration for future exercise session storage.

#### Kamogelo Melvin Seilane

Responsible for:

* Meal Planning Module.
* Nutritionix API integration.
* Meal tracking and calorie calculations.
* Firestore meal data management.
* Daily calorie goal functionality.
* Nutrition-related user interfaces and logic.

Working collaboratively allowed us to integrate both modules seamlessly while gaining valuable experience in teamwork, problem-solving, and software engineering principles.

---

##  Features

* Secure Firebase Authentication.
* Firestore database integration.
* Smart Meal Planning.
* Daily calorie goal tracking.
* Exercise and workout monitoring.
* Real-time weather information for outdoor training.
* Light and Dark mode support.
* Multi-language functionality.
* Firestore synchronization.
* Offline storage support (planned).
* Push notification support (planned).

---

##  Meal Planning Features

* Search meals using Nutritionix.
* View calories and nutritional information.
* Track daily food consumption.
* Compare calorie intake against personalized goals.
* Store meal history securely using Firestore.
* Future offline meal caching support.

---

##  Exercise Features

* Track workout sessions and exercise goals.
* Monitor total training time.
* View progress indicators for daily goals.
* Display real-time weather information for outdoor activities.
* Personalized exercise summaries.
* Future workout synchronization support.

---

##  API Integration

Healthify utilizes two external APIs to provide intelligent and real-time functionality.

### Nutritionix API

Responsible for:

* Meal searches.
* Calorie calculations.
* Nutritional information.
* Macronutrient breakdown.

This API powers the Meal Planning module developed by Kamogelo Melvin Seilane.

### OpenWeather API

Responsible for:

* Real-time weather information.
* Outdoor exercise planning.
* Temperature and weather condition updates.

This API powers the Exercise module developed by Thabiso Shaun Charles Makhobo.

Both APIs significantly enhance the user experience by providing reliable and dynamic information.

---

##  API Keys & Security

Healthify makes use of API keys to communicate securely with third-party services.

Sensitive information such as:

* Nutritionix API credentials.
* OpenWeather API credentials.
* Firebase configuration details.
* Environment-specific secrets.

are intentionally excluded from the public repository.

Developers wishing to run this project locally must configure their own API credentials before building the application.

No sensitive credentials should be committed to GitHub repositories.

---

##  Technologies Used

* Kotlin
* Android Studio
* Firebase Authentication
* Firebase Firestore
* Retrofit
* Gson
* OkHttp
* Nutritionix API
* OpenWeather API
* Material Design Components
* Room Database (Planned)
* GitHub Actions
* JUnit
* Mockito
* Git & GitHub

---

## ⚙️ Installation

1. Clone the repository:

```bash
git clone https://github.com/MelvinSeilane02/Healthify.git
```

2. Configure Firebase services.

3. Add your own API credentials for:

* Nutritionix API
* OpenWeather API

4. Sync Gradle dependencies.

5. Build and run the application using Android Studio.

---

##  Future Improvements

Planned improvements include:

* Google Fit integration.
* Offline synchronization using RoomDB.
* Advanced fitness analytics.
* Push notifications using Firebase Cloud Messaging.
* Enhanced workout visualizations.
* Monthly wellness reports.
* Additional multilingual support.
* Improved graphical data representations.

---

##  Testing & CI/CD

Healthify utilizes:

* JUnit Testing
* Mockito
* GitHub Actions

Automated workflows are configured to:

* Build the Android project.
* Run unit tests.
* Verify project integrity across environments.

---

##  AI Tools Utilized

AI tools such as ChatGPT (OpenAI) were utilized during development to assist with:

* Kotlin debugging.
* API integration guidance.
* Technical documentation.
* Firestore database design suggestions.
* Localization setup.
* Software engineering best practices.

All AI-generated suggestions were reviewed, modified, tested, and validated before implementation. No code was included without developer verification and customization.

---

##  Demonstration

Healthify Prototype:

https://youtu.be/RipgBzHuHmc

---

##  Credits

Developed by:

* **Thabiso Shaun Charles Makhobo**
* **Kamogelo Melvin Seilane**

*Rosebank College – Diploma in Information Technology (Software Development) Project*

Healthify reflects not only our technical abilities as software developers but also our ability to collaborate effectively on a modular software solution. The project demonstrates practical experience in Android development, API integration, cloud databases, authentication systems, and collaborative problem-solving while addressing real-world health and wellness challenges.

---

##  License

This project is licensed under the MIT License. See the LICENSE file for additional information.
