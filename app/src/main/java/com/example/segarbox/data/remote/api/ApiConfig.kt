package com.example.segarbox.data.remote.api

import com.example.segarbox.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {

    companion object {
        var BASE_URL = BuildConfig.BASE_URL_GOOGLE_MAPS

        fun getApiServices(): ApiServices {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiServices::class.java)
        }
    }

}