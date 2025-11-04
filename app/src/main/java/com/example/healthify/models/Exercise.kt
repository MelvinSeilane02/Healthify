package com.example.healthify.models

import com.google.gson.annotations.SerializedName

data class Exercise(
    val id: String,
    val name: String,
    val bodyPart: String,
    val target: String,
    val equipment: String,
    @SerializedName("gifUrl") val gifUrl: String? = null
)

