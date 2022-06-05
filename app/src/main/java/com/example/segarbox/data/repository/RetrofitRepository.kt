package com.example.segarbox.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.beust.klaxon.Klaxon
import com.example.segarbox.BuildConfig
import com.example.segarbox.data.local.model.MakeOrderBody
import com.example.segarbox.data.local.model.ProductTransactions
import com.example.segarbox.data.local.model.UpdateStatusBody
import com.example.segarbox.data.remote.api.ApiConfig
import com.example.segarbox.data.remote.api.ApiServices
import com.example.segarbox.data.remote.response.*
import com.example.segarbox.ui.paging.ProductPagingSource


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

    suspend fun getProductById(id: Int): ProductByIdResponse {
        try {
            val request = segarBoxApiServices.getProductById(id)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            return ProductByIdResponse()

        } catch (ex: Exception) {
            return ProductByIdResponse()
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
            return ProductResponse(listOf())

        } catch (ex: Exception) {
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

    suspend fun logout(token: String): LogoutResponse {
        try {
            val request = segarBoxApiServices.logout(token)
            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            return LogoutResponse()
        } catch (ex: Exception) {
            return LogoutResponse()
        }
    }

    suspend fun addCart(token: String, productId: Int, productQty: Int): AddCartResponse {
        try {
            val request = segarBoxApiServices.addToCart(token, productId, productQty)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }

            val result = Klaxon().parse<AddCartResponse>(request.errorBody()!!.string())
            return result!!

        } catch (ex: Exception) {
            return AddCartResponse()
        }
    }

    suspend fun getUserCart(token: String): UserCartResponse {
        try {
            val request = segarBoxApiServices.getUserCart(token)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            val result = Klaxon().parse<UserCartResponse>(request.errorBody()!!.string())
            return result!!

        } catch (ex: Exception) {
            return UserCartResponse(message = ex.message)
        }
    }

    suspend fun getIsCheckedUserCart(token: String): UserCartResponse {
        try {
            val request = segarBoxApiServices.getIsCheckedUserCart(token)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            return UserCartResponse()

        } catch (ex: Exception) {
            return UserCartResponse()
        }
    }

    suspend fun deleteUserCart(token: String, cartId: Int): DeleteCartResponse {
        try {
            val request = segarBoxApiServices.deleteUserCart(token, cartId)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            return DeleteCartResponse()

        } catch (ex: Exception) {
            return DeleteCartResponse(message = ex.message.toString())
        }
    }

    suspend fun updateUserCart(
        token: String,
        cartId: Int,
        productId: Int,
        productQty: Int,
        isChecked: Int,
    ): UpdateCartResponse {
        try {
            val request =
                segarBoxApiServices.updateUserCart(token, cartId, productId, productQty, isChecked)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            val result = Klaxon().parse<UpdateCartResponse>(request.errorBody()!!.string())
            return result!!

        } catch (ex: Exception) {
            return UpdateCartResponse()
        }
    }

    suspend fun getCartDetail(token: String, shippingCost: Int): CartDetailResponse {
        try {
            val request = segarBoxApiServices.getCartDetail(token, shippingCost)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            return CartDetailResponse()

        } catch (ex: Exception) {
            return CartDetailResponse()
        }
    }

    suspend fun saveAddress(
        token: String,
        street: String,
        city: String,
        postalCode: String,
    ): AddAddressResponse {
        try {
            val request = segarBoxApiServices.saveAddress(token, street, city, postalCode)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            return AddAddressResponse()

        } catch (ex: Exception) {
            return AddAddressResponse(message = ex.message.toString())
        }
    }

    suspend fun getUserAddresses(token: String): GetAddressResponse {
        try {
            val request = segarBoxApiServices.getUserAddresses(token)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            return GetAddressResponse()

        } catch (ex: Exception) {
            return GetAddressResponse(message = ex.message.toString())
        }
    }

    suspend fun deleteAddress(token: String, addressId: Int): DeleteAddressResponse {
        try {
            val request = segarBoxApiServices.deleteAddress(token, addressId)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            return DeleteAddressResponse()

        } catch (ex: Exception) {
            return DeleteAddressResponse(message = ex.message.toString())
        }
    }

    suspend fun makeOrderTransaction(token: String, makeOrderBody: MakeOrderBody): MakeOrderResponse {
        try {
            val request = segarBoxApiServices.makeOrderTransaction(token, makeOrderBody)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            val result = Klaxon().parse<MakeOrderResponse>(request.errorBody()!!.string())
            return result!!

        } catch (ex: Exception) {
            return MakeOrderResponse(message = ex.message.toString())
        }
    }

    suspend fun getTransactionById(token: String, transactionId :Int): TransactionByIdResponse {
        try {
            val request = segarBoxApiServices.getTransactionById(token, transactionId)

            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            val result = Klaxon().parse<TransactionByIdResponse>(request.errorBody()!!.string())
            return result!!

        } catch (ex: Exception) {
            return TransactionByIdResponse(message = ex.message.toString())
        }
    }

    suspend fun getTransactions(token: String, status: String): TransactionsResponse {
        try {
            val request = segarBoxApiServices.getTransactions(token, status)
            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            val result = Klaxon().parse<TransactionsResponse>(request.errorBody()!!.string())
            return result!!

        } catch (ex: Exception) {
            return TransactionsResponse(message = ex.message.toString())
        }
    }

    suspend fun updateTransactionStatus(token: String, transactionId: Int, updateStatusBody: UpdateStatusBody): TransactionsStatusResponse {
        try {
            val request = segarBoxApiServices.updateTransactionStatus(token, transactionId, updateStatusBody)
            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            val result = Klaxon().parse<TransactionsStatusResponse>(request.errorBody()!!.string())
            return result!!

        } catch (ex: Exception) {
            return TransactionsStatusResponse(message = ex.message.toString())
        }
    }

    suspend fun getRatings(token: String): RatingResponse {
        try {
            val request = segarBoxApiServices.getRatings(token)
            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            val result = Klaxon().parse<RatingResponse>(request.errorBody()!!.string())
            return result!!

        } catch (ex: Exception) {
            return RatingResponse(message = ex.message.toString())
        }
    }

    suspend fun saveRating(token: String, ratingId: Int, transactionId: Int, productId: Int, rating: Int): SaveRatingResponse {
        try {
            val request = segarBoxApiServices.saveRating(token, ratingId, transactionId, productId, rating)
            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            }
            val result = Klaxon().parse<SaveRatingResponse>(request.errorBody()!!.string())
            return result!!

        } catch (ex: Exception) {
            return SaveRatingResponse(message = ex.message.toString())
        }
    }


}