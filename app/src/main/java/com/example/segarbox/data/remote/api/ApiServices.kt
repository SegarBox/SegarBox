package com.example.segarbox.data.remote.api

import com.example.segarbox.BuildConfig
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.data.remote.response.CityResponse
import com.example.segarbox.data.remote.response.MapsResponse
import com.example.segarbox.data.remote.response.ShippingResponse
import retrofit2.Response
import retrofit2.http.*

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

    @FormUrlEncoded
    @POST("cost")
    suspend fun getShippingCosts(
        @Header("key") key: String = BuildConfig.RAJAONGKIR_KEY,
        @Field("origin") origin: String = Code.SEMARANG_ID,
        @Field("destination") destination: String,
        @Field("weight") weight: String,
        @Field("courier") courier: String
    ): Response<ShippingResponse>
}