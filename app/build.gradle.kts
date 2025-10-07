// 'findProperty' returns 'Any?', so we cast it to 'String?'.
/**********CHANGE THIS API KEY TO YOUR OWN*************/
val nutritionixAppId = "f5d73b4b"
val nutritionixAppKey = "031e9e6bc34302c050c343c01cea5ea7"
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

    // 🔥 Firebase libraries
    implementation(libs.firebase.firestore)
    implementation("com.google.firebase:firebase-auth:24.0.1")

    // Networking (Retrofit + Gson)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Image loading (for weather icons)
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation(libs.androidx.media3.common.ktx)
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    //fitness
    implementation("com.google.android.gms:play-services-fitness:21.1.0")
    implementation("com.google.android.gms:play-services-auth:21.1.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.google.android.material:material:1.12.0")

    //Nutritionix API
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.1.0")

    implementation("com.squareup.okhttp3:okhttp:5.1.0")
    implementation("com.squareup.picasso:picasso:2.8")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
