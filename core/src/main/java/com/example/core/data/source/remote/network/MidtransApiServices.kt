package com.example.core.data.source.remote.network

import com.example.core.BuildConfig
import com.example.core.data.source.remote.response.MidtransStatusResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface MidtransApiServices {

    @GET("{order_id}/status")
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "Authorization: Basic ${BuildConfig.MIDTRANS_SERVER_KEY}"
    )
    suspend fun getMidtransStatus(
        @Path("order_id") orderId: String
    ): Response<MidtransStatusResponse>

}