package com.example.segarbox.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.beust.klaxon.Klaxon
import com.example.segarbox.BuildConfig
import com.example.segarbox.data.local.database.MainDatabase
import com.example.segarbox.data.remote.api.ApiConfig
import com.example.segarbox.data.remote.api.ApiServices
import com.example.segarbox.data.remote.response.*
import com.example.segarbox.ui.paging.ProductRemoteMediator


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

    suspend fun login(
        email: String,
        password: String
    ): LoginResponse {
        try {
            val request = segarBoxApiServices.login(email, password)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }

            val result = Klaxon().parse<LoginResponse>(request.errorBody()!!.string())
            return result!!

        } catch (ex: Exception) {
            return LoginResponse(message = ex.message.toString())
        }
    }

    fun getAllProduct(
        apiServices: ApiServices,
        database: MainDatabase,
    ): LiveData<PagingData<ProductItem>> {

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = ProductRemoteMediator(
                apiServices = apiServices,
                database = database),
            pagingSourceFactory = {
                database.productDao().getAllProduct()
            }
        ).liveData

    }


}