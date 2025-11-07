package com.example.healthify.api

import com.example.healthify.models.Exercise
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ExerciseApiService {

    @Headers(
        "x-rapidapi-key: 3f7e508fadmsh959f9d383b10369p11ec0bjsnf6434848fca6",
        "x-rapidapi-host: exercisedb.p.rapidapi.com"
    )
    @GET("exercises")
    fun getAllExercises(): Call<List<Exercise>>

    // ✅ Correct way: use @Path instead of @Query
    @Headers(
        "x-rapidapi-key: 3f7e508fadmsh959f9d383b10369p11ec0bjsnf6434848fca6",
        "x-rapidapi-host: exercisedb.p.rapidapi.com"
    )
    @GET("exercises/bodyPart/{bodyPart}")
    fun getExercisesByBodyPart(
        @Path("bodyPart") bodyPart: String
    ): Call<List<Exercise>>

    @Headers(
        "x-rapidapi-key: 3f7e508fadmsh959f9d383b10369p11ec0bjsnf6434848fca6",
        "x-rapidapi-host: exercisedb.p.rapidapi.com"
    )
    @GET("exercises/target/{target}")
    fun getExercisesByTarget(
        @Path("target") target: String
    ): Call<List<Exercise>>
}
