// 'findProperty' returns 'Any?', so we cast it to 'String?'.
/**********CHANGE THIS API KEY TO YOUR OWN*************/
val nutritionixAppId = "fd6a36c3"
val nutritionixAppKey = "0ac4c30b12a16d058670983365d966b0"
/********************************************************/

// Add this check. If either key is null (not found), the build will fail with a clear error message.
if (nutritionixAppId.isNullOrBlank() || nutritionixAppKey.isNullOrBlank()) {
    throw InvalidUserDataException(
        "NUTRITIONIX_APP_ID and NUTRITIONIX_APP_KEY are not defined or are empty in your gradle.properties file. " +
                "Please add them to continue."
    )
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    //kotlin("android.extensions")
    /*id("com.android.application") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false*/

}

android {
    namespace = "com.example.healthify"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.healthify"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Now use the validated, non-null properties to create the buildConfigFields.
        buildConfigField("String",
            "NUTRITIONIX_APP_ID",
            "\"$nutritionixAppId\"")
        buildConfigField("String",
            "NUTRITIONIX_APP_KEY",
            "\"$nutritionixAppKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    // Configure Java 11 compatibility
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

    /*kotlin {
        jvmToolchain(11)
    }*/

    /*kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(11))
        }
    }*/

    /*kotlinOptions {
        jvmTarget = "11"
    }*/

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    buildFeatures {
        viewBinding = true
    }

    buildFeatures {
        dataBinding = true
    }

    // ✅ Enable BuildConfig generation
    buildFeatures {
        buildConfig = true
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // 🔥 Firebase (using BoM to handle versions automatically)
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")


    // Networking (Retrofit + Gson)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Image loading (for weather icons)
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation(libs.androidx.media3.common.ktx)
    implementation(libs.junit.junit)
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    //fitness
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("io.coil-kt:coil:2.4.0") // For loading images
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.material:material:1.12.0")


    //Nutritionix API
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.1.0")
    implementation("com.squareup.okhttp3:okhttp:5.1.0")
    implementation("com.squareup.picasso:picasso:2.8")

    // --- Unit Testing ---
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.11.0")
   // testImplementation("org.mockito:mockito-inline:5.11.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1")


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
