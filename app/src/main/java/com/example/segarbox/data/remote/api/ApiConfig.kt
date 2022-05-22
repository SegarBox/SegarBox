package com.example.segarbox.data.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {

    companion object {
        var BASE_URL = "https://maps.googleapis.com/maps/api/geocode/"

        fun getApiServices(): ApiServices {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiServices::class.java)
        }
    }

}