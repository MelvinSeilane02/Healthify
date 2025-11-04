package com.example.healthify.api

import com.example.healthify.models.Exercise
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ExerciseApi {

    // ✅ Fetch exercises by body part (muscle)
    @Headers(
        "x-rapidapi-host: exercisedb.p.rapidapi.com",
        "x-rapidapi-key: 3f7e508fadmsh959f9d383b10369p11ec0bjsnf6434848fca6"
    )
    @GET("exercises/bodyPart/{bodyPart}")
    fun getExercisesByMuscle(
        @Path("bodyPart") bodyPart: String
    ): Call<List<Exercise>>
}
