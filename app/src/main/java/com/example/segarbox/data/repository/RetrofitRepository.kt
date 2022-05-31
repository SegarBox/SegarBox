package com.example.segarbox.data.repository

import android.util.Log
import android.util.Size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.beust.klaxon.Klaxon
import com.example.segarbox.BuildConfig
import com.example.segarbox.data.local.database.MainDatabase
import com.example.segarbox.data.remote.api.ApiConfig
import com.example.segarbox.data.remote.api.ApiServices
import com.example.segarbox.data.remote.response.*
import com.example.segarbox.ui.paging.ProductPagingSource
import com.example.segarbox.ui.paging.ProductRemoteMediator
import java.sql.DataTruncation


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
        password: String,
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

    fun getProductPaging(
        apiServices: ApiServices,
        filter: String,
        filterValue: String,
    ): LiveData<PagingData<ProductItem>> {

        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                ProductPagingSource(apiServices, filter, filterValue)
            }
        ).liveData

    }

    suspend fun getAllProduct(page: Int, size: Int): ProductResponse {
        try {
            val request = segarBoxApiServices.getAllProduct(page, size)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }

            return ProductResponse(listOf())

        } catch (ex: Exception) {
            return ProductResponse(listOf())
        }
    }

    suspend fun getCategoryProduct(page: Int, size: Int, category: String): ProductResponse {
        try {
            val request = segarBoxApiServices.getCategoryProduct(page, size, category)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }

            return ProductResponse(listOf())

        } catch (ex: Exception) {
            return ProductResponse(listOf())
        }
    }

    suspend fun getLabelProduct(page: Int, size: Int, label: String): ProductResponse {
        try {
            val request = segarBoxApiServices.getLabelProduct(page, size, label)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            Log.e("ERROR LABEL", request.errorBody()!!.string())
            return ProductResponse(listOf())

        } catch (ex: Exception) {
            Log.e("ERROR LABEL CATCH", ex.message.toString())
            return ProductResponse(listOf())
        }
    }

    suspend fun getUser(token: String): UserResponse {

        try {
            val request = segarBoxApiServices.getUser(token)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            return UserResponse()

        } catch (ex: Exception) {
            return UserResponse()
        }
    }

//    suspend fun getUser(token: String, id: Int): UserResponse {
//
//        try {
//            val request = segarBoxApiServices.getUser(token, id)
//
//            if (request.isSuccessful) {
//                request.body()?.let {
//                    return it
//                }
//            }
//            return UserResponse()
//
//        } catch (ex: Exception) {
//            return UserResponse()
//        }
//    }

//    suspend fun logout(): LogoutResponse {
//
//        try {
//            val request = segarBoxApiServices.logout()
//
//            if (request.isSuccessful) {
//                request.body()?.let {
//                    return it
//                }
//            }
//            return LogoutResponse()
//
//        } catch (ex: Exception) {
//            return LogoutResponse()
//        }
//    }


}