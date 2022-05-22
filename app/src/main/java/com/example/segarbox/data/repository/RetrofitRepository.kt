package com.example.segarbox.data.repository

import com.example.segarbox.data.remote.api.ApiConfig
import com.example.segarbox.data.remote.response.MapsResponse

class RetrofitRepository {

    private val apiServices = ApiConfig.getApiServices()

    suspend fun getAddress(latLng: String): MapsResponse {
        try {
            val request = apiServices.getAddress(latLng)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            return MapsResponse(status = request.code().toString())

        } catch (ex: Exception) {
            return MapsResponse(status = ex.message.toString())
        }
    }

}