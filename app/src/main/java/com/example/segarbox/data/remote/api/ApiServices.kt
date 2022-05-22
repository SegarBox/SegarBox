package com.example.segarbox.data.remote.api

import com.example.segarbox.data.remote.response.MapsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiServices {

    @GET("json")
    suspend fun getAddress(
        @Query("latlng") latLng: String,
        @Query("key") apiKey: String = "AIzaSyDSO2eRuojg7bxvMqikBKwfoFyHKgTTqHg"
    ): Response<MapsResponse>

}