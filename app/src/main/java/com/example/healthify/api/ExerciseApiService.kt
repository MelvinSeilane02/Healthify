
package com.example.healthify.api

import com.example.healthify.models.Exercise
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ExerciseApiService {

    // If you want to keep a single header set for the API:
    @Headers(
        "x-rapidapi-key: 3f7e508fadmsh959f9d383b10369p11ec0bjsnf6434848fca6",
        "x-rapidapi-host: exercisedb.p.rapidapi.com"
    )
    @GET("exercises")
    fun getAllExercises(): Call<List<Exercise>>

    @Headers(
        "x-rapidapi-key: 3f7e508fadmsh959f9d383b10369p11ec0bjsnf6434848fca6",
        "x-rapidapi-host: exercisedb.p.rapidapi.com"
    )
    @GET("exercises")
    fun getExercisesByMuscle(
        @Query("bodyPart") bodyPart: String
    ): Call<List<Exercise>>

    // Optional: search by target muscle
    @Headers(
        "x-rapidapi-key: 3f7e508fadmsh959f9d383b10369p11ec0bjsnf6434848fca6",
        "x-rapidapi-host: exercisedb.p.rapidapi.com"
    )
    @GET("exercises")
    fun getExercisesByTarget(
        @Query("target") target: String
    ): Call<List<Exercise>>
}
