package com.example.segarbox.data.remote.api

import com.example.segarbox.BuildConfig
import com.example.segarbox.data.remote.response.CityResponse
import com.example.segarbox.data.remote.response.MapsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiServices {

    @GET("json")
    suspend fun getAddress(
        @Query("latlng") latLng: String,
        @Query("key") apiKey: String = BuildConfig.GOOGLE_MAPS_KEY
    ): Response<MapsResponse>

    @GET("city")
    suspend fun getCity(
        @Header("key") key: String = BuildConfig.RAJAONGKIR_KEY
    ): Response<CityResponse>
}