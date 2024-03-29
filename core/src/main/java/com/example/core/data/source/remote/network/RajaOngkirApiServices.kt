package com.example.core.data.source.remote.network

import com.example.core.BuildConfig
import com.example.core.data.source.remote.response.CityResponse
import com.example.core.data.source.remote.response.ShippingResponse
import com.example.core.utils.Code
import retrofit2.Response
import retrofit2.http.*

interface RajaOngkirApiServices {

    @GET("city")
    suspend fun getCity(
        @Header("key") key: String = BuildConfig.RAJAONGKIR_KEY,
    ): Response<CityResponse>

    @FormUrlEncoded
    @POST("cost")
    suspend fun getShippingCosts(
        @Header("key") key: String = BuildConfig.RAJAONGKIR_KEY,
        @Field("origin") origin: String = Code.JAKARTA_ID,
        @Field("destination") destination: String,
        @Field("weight") weight: String,
        @Field("courier") courier: String,
    ): Response<ShippingResponse>

}