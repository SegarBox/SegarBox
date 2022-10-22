package com.example.segarbox.core.data.source.remote.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FlaskApiServices {

    @GET("recommendation")
    @Headers("Accept: application/json")
    suspend fun getRecommendationSystem(
        @Query("user_id") userId: Int = 0
    ): Response<List<String>>

}