package com.example.segarbox.data.repository

import android.util.Log
import com.beust.klaxon.Klaxon
import com.example.segarbox.BuildConfig
import com.example.segarbox.data.remote.api.ApiConfig
import com.example.segarbox.data.remote.response.*
import okhttp3.ResponseBody


class RetrofitRepository {

    private val mapsApiServices = ApiConfig.getApiServices(BuildConfig.BASE_URL_GOOGLE_MAPS)
    private val rajaOngkirApiServices = ApiConfig.getApiServices(BuildConfig.BASE_URL_RAJAONGKIR)
    private val segarBoxApiServices = ApiConfig.getApiServices(BuildConfig.BASE_URL_SEGARBOX)

    suspend fun getAddress(latLng: String): MapsResponse {

        try {
            val request = mapsApiServices.getAddress(latLng)

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

    suspend fun getCityFromApi(): CityResponse {
        try {
            val request = rajaOngkirApiServices.getCity()

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            return CityResponse(Rajaongkir(status = Status(request.code(), request.message())))

        } catch (ex: Exception) {
            return CityResponse(Rajaongkir(status = Status(description = ex.message.toString())))
        }
    }

    suspend fun getShippingCosts(
        destination: String,
        weight: String,
        courier: String,
    ): ShippingResponse {
        try {
            val request = rajaOngkirApiServices.getShippingCosts(
                destination = destination,
                weight = weight,
                courier = courier
            )

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            return ShippingResponse()
        } catch (ex: Exception) {
            return ShippingResponse()
        }
    }

    suspend fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        password_confirmation: String,
    ): RegisterResponse {
        try {
            val request =
                segarBoxApiServices.register(name, email, phone, password, password_confirmation)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }

            val result = Klaxon().parse<RegisterResponse>(request.errorBody()!!.string())
            return result!!

        } catch (ex: Exception) {
            return RegisterResponse(message = ex.message.toString())
        }
    }


}