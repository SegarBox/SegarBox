package com.example.core.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.beust.klaxon.Klaxon
import com.example.core.data.Resource
import com.example.core.data.paging.ProductPagingSource
import com.example.core.data.source.remote.network.*
import com.example.core.data.source.remote.response.*
import com.example.core.domain.body.MakeOrderBody
import com.example.core.domain.body.UpdateStatusBody
import com.example.core.domain.model.*
import com.example.core.domain.model.Shipping
import com.example.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val segarBoxApiServices: SegarBoxApiServices,
    private val mapsApiServices: MapsApiServices,
    private val rajaOngkirApiServices: RajaOngkirApiServices,
    private val flaskApiServices: FlaskApiServices,
) {

    suspend fun getUserAddresses(token: String): Flow<Resource<List<Address>>> = flow {
        emit(Resource.Loading())
        try {
            val request = segarBoxApiServices.getUserAddresses(token)

            if (request.isSuccessful) {
                request.body()?.let { response ->
                    val data = DataMapper.mapGetAddressResponseToAddresses(response)
                    if (!data.isNullOrEmpty()) {
                        emit(Resource.Success(data))
                    } else {
                        emit(Resource.Empty())
                    }
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't access user address"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't access user address"))
        }
    }

    suspend fun deleteAddress(token: String, addressId: Int): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val request = segarBoxApiServices.deleteAddress(token, addressId)

            if (request.isSuccessful) {
                request.body()?.let {
                    emit(Resource.Success("Delete address success"))
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't delete address"))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't delete Address"))
        }
    }

    suspend fun getCart(token: String): Flow<Resource<List<Cart>>> = flow {
        emit(Resource.Loading())
        try {
            val request = segarBoxApiServices.getUserCart(token)

            if (request.isSuccessful) {
                request.body()?.let { response ->
                    val data = DataMapper.mapCartResponseToCarts(response)
                    if (!data.isNullOrEmpty()) {
                        emit(Resource.Success(data))
                    } else {
                        emit(Resource.Empty())
                    }
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't access cart"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't access cart"))
        }
    }

    suspend fun getCheckedCart(token: String): Flow<Resource<List<Cart>>> = flow {
        emit(Resource.Loading())
        try {
            val request = segarBoxApiServices.getIsCheckedUserCart(token)

            if (request.isSuccessful) {
                request.body()?.let { response ->
                    val data = DataMapper.mapCartResponseToCarts(response)
                    if (!data.isNullOrEmpty()) {
                        emit(Resource.Success(data))
                    } else {
                        emit(Resource.Empty())
                    }
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't access cart"))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't access cart"))
        }
    }

    suspend fun addCart(token: String, productId: Int, productQty: Int): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val request = segarBoxApiServices.addToCart(token, productId, productQty)

            if (request.isSuccessful) {
                request.body()?.let {
                    emit(Resource.Success("Add Cart Success"))
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't add cart"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't add cart"))
        }
    }

    suspend fun deleteCart(token: String, cartId: Int): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val request = segarBoxApiServices.deleteUserCart(token, cartId)

            if (request.isSuccessful) {
                request.body()?.let {
                    emit(Resource.Success("Delete cart success"))
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't delete cart"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't delete cart"))
        }
    }

    suspend fun updateCart(
        token: String,
        cartId: Int,
        productId: Int,
        productQty: Int,
        isChecked: Int,
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val request =
                segarBoxApiServices.updateUserCart(token, cartId, productId, productQty, isChecked)

            if (request.isSuccessful) {
                request.body()?.let {
                    emit(Resource.Success("Update cart success"))
                }
            } else {
                val result = Klaxon().parse<UpdateCartResponse>(request.errorBody()!!.string())
                emit(Resource.Error(message = result!!.message
                    ?: "Something went wrong, can't update cart"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't update cart"))
        }
    }

    suspend fun getCartDetail(token: String, shippingCost: Int): Flow<Resource<CartDetail>> = flow {
        emit(Resource.Loading())
        try {
            val request = segarBoxApiServices.getCartDetail(token, shippingCost)

            if (request.isSuccessful) {
                request.body()?.let { response ->
                    emit(Resource.Success(DataMapper.mapCartDetailResponseToCartDetail(response)))
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't access cart details"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't access cart details"))
        }
    }

    suspend fun makeOrder(token: String, makeOrderBody: MakeOrderBody): Flow<Resource<MakeOrder>> =
        flow {
            emit(Resource.Loading())
            try {
                val request = segarBoxApiServices.makeOrderTransaction(token, makeOrderBody)

                if (request.isSuccessful) {
                    request.body()?.let { response ->
                        val data = DataMapper.mapMakeOrderResponseToMakeOrder(response)
                        if (data != null) {
                            emit(Resource.Success(data))
                        } else {
                            emit(Resource.Error(message = "Something went wrong, can't make order"))
                        }
                    }
                } else {
                    val result = Klaxon().parse<MakeOrderResponse>(request.errorBody()!!.string())
                    emit(Resource.Error(message = result!!.message
                        ?: "Something went wrong, can't make order"))
                }

            } catch (ex: Exception) {
                emit(Resource.Error(message = "Something went wrong, can't make order"))
            }
        }

    suspend fun getProductById(id: Int): Flow<Resource<Product>> = flow {
        emit(Resource.Loading())
        try {
            val request = segarBoxApiServices.getProductById(id)

            if (request.isSuccessful) {
                request.body()?.let { response ->
                    val data = DataMapper.mapProductByIdResponseToProduct(response)
                    if (data != null) {
                        emit(Resource.Success(data))
                    } else {
                        emit(Resource.Error(message = "Something went wrong, can't access product"))
                    }
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't access product"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't access product"))
        }
    }

    suspend fun getCityFromApi(): Flow<Resource<List<City>>> = flow {
        emit(Resource.Loading())
        try {
            val request = rajaOngkirApiServices.getCity()
            if (request.isSuccessful) {
                request.body()?.let { response ->
                    val data = DataMapper.mapCityResponseToCities(response)
                    if (data.isNotEmpty()) {
                        emit(Resource.Success(data))
                    } else {
                        emit(Resource.Empty())
                    }
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't access city"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't access city"))
        }
    }

    fun getAllProducts(page: Int, size: Int): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        try {
            val request = segarBoxApiServices.getAllProduct(page, size)

            if (request.isSuccessful) {
                request.body()?.let { response ->
                    val data = DataMapper.mapProductResponseToProducts(response)
                    if (data.isNotEmpty()) {
                        emit(Resource.Success(data))
                    } else {
                        emit(Resource.Empty())
                    }
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't access products"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't access products"))
        }
    }

    fun getProductByCategory(
        page: Int,
        size: Int,
        category: String,
    ): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        try {
            val request = segarBoxApiServices.getCategoryProduct(page, size, category)

            if (request.isSuccessful) {
                request.body()?.let { response ->
                    val data = DataMapper.mapProductResponseToProducts(response)
                    if (data.isNotEmpty()) {
                        emit(Resource.Success(data))
                    } else {
                        emit(Resource.Empty())
                    }
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't access products"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't access products"))
        }
    }

    fun getTransactionById(token: String, transactionId: Int): Flow<Resource<Transaction>> = flow {
        emit(Resource.Loading())
        try {
            val request = segarBoxApiServices.getTransactionById(token, transactionId)

            if (request.isSuccessful) {
                request.body()?.let { response ->
                    val data = DataMapper.mapTransactionByIdResponseToTransaction(response)
                    if (data != null) {
                        emit(Resource.Success(data))
                    } else {
                        emit(Resource.Error(message = "Something went wrong, can't access your transaction"))
                    }
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't access your transaction"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't access your transaction"))
        }
    }

    fun getUser(token: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val request = segarBoxApiServices.getUser(token)

            if (request.isSuccessful) {
                request.body()?.let { response ->
                    val data = DataMapper.mapUserResponseToUser(response)
                    if (data != null) {
                        emit(Resource.Success(data))
                    } else {
                        emit(Resource.Error(message = "Something went wrong, can't access user"))
                    }
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't access user"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't access user"))
        }
    }

    fun updateTransactionStatus(
        token: String,
        transactionId: Int,
        updateStatusBody: UpdateStatusBody,
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val request =
                segarBoxApiServices.updateTransactionStatus(token, transactionId, updateStatusBody)
            if (request.isSuccessful) {
                request.body()?.let { response ->
                    response.message?.let {
                        emit(Resource.Success(it))
                    }
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't update transaction"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't update transaction"))
        }
    }

    fun login(email: String, password: String): Flow<Resource<Login>> = flow {
        emit(Resource.Loading())
        try {
            val request = segarBoxApiServices.login(email, password)

            if (request.isSuccessful) {
                request.body()?.let { response ->
                    val data = DataMapper.mapLoginResponseToLogin(response)
                    emit(Resource.Success(data))
                }
            } else {
                val result = Klaxon().parse<LoginResponse>(request.errorBody()!!.string())
                val mappedResult = DataMapper.mapLoginResponseToLogin(result!!)
                emit(Resource.Error(data = mappedResult, message = ""))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = ex.message ?: "Something went wrong, can't login"))
        }
    }


    fun getAddress(latLng: String): Flow<Resource<List<Maps>>> = flow {
        emit(Resource.Loading())
        try {
            val request = mapsApiServices.getAddress(latLng)

            if (request.isSuccessful) {
                request.body()?.let { response ->
                    val data = DataMapper.mapMapsResponseToMaps(response)
                    if (!data.isNullOrEmpty()) {
                        emit(Resource.Success(data))
                    } else {
                        emit(Resource.Error(message = "Something went wrong, can't access address"))
                    }
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't access address"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't access address"))
        }
    }

    fun saveAddress(
        token: String,
        street: String,
        city: String,
        postalCode: String,
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val request = segarBoxApiServices.saveAddress(token, street, city, postalCode)

            if (request.isSuccessful) {
                request.body()?.let { response ->
                    response.info?.let {
                        emit(Resource.Success(it))
                    }
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't save address"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't save address"))
        }
    }

    fun getRatings(token: String): Flow<Resource<List<Rating>>> = flow {
        emit(Resource.Loading())
        try {
            val request = segarBoxApiServices.getRatings(token)
            if (request.isSuccessful) {
                request.body()?.let { response ->
                    val data = DataMapper.mapRatingResponseToRatings(response)
                    if (!data.isNullOrEmpty()) {
                        emit(Resource.Success(data))
                    } else {
                        emit(Resource.Empty())
                    }
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't get ratings"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't get ratings"))
        }
    }

    fun saveRating(
        token: String,
        ratingId: Int,
        transactionId: Int,
        productId: Int,
        rating: Int,
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val request =
                segarBoxApiServices.saveRating(token, ratingId, transactionId, productId, rating)

            if (request.isSuccessful) {
                request.body()?.let {
                    emit(Resource.Success("Save rating success"))
                }
            } else {
                emit(Resource.Error("Something went wrong, can't save rating"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error("Something went wrong, can't save rating"))
        }
    }

    fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        password_confirmation: String,
    ): Flow<Resource<Register>> = flow {
        emit(Resource.Loading())
        try {
            val request =
                segarBoxApiServices.register(name, email, phone, password, password_confirmation)

            if (request.isSuccessful) {
                request.body()?.let { response ->
                    val data = DataMapper.mapRegisterResponseToRegister(response)
                    emit(Resource.Success(data))
                }
            } else {
                val result = Klaxon().parse<RegisterResponse>(request.errorBody()!!.string())
                val mappedResult = DataMapper.mapRegisterResponseToRegister(result!!)
                emit(Resource.Error(data = mappedResult, message = ""))
            }

        } catch (ex: Exception) {
            emit(Resource.Error("Something went wrong, can't register account"))
        }
    }

    fun getProductPaging(
        filter: String,
        filterValue: String,
    ): Flow<PagingData<ProductItem>> =
        Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                ProductPagingSource(segarBoxApiServices, filter, filterValue)
            }
        ).flow

    fun getShippingCosts(
        destination: String,
        weight: String,
        courier: String,
    ): Flow<Resource<List<Shipping>>> = flow {
        emit(Resource.Loading())
        try {
            val request = rajaOngkirApiServices.getShippingCosts(
                destination = destination,
                weight = weight,
                courier = courier
            )

            if (request.isSuccessful) {
                request.body()?.let { response ->
                    val data = DataMapper.mapShippingResponseToShipping(response)
                    if (!data.isNullOrEmpty()) {
                        emit(Resource.Success(data))
                    } else {
                        emit(Resource.Empty())
                    }
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't get shipping costs"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't get shipping costs"))
        }
    }

    fun getTransactions(token: String, status: String): Flow<Resource<List<Transaction>>> = flow {
        emit(Resource.Loading())
        try {
            val request = segarBoxApiServices.getTransactions(token, status)
            if (request.isSuccessful) {
                request.body()?.let { response ->
                    val data = DataMapper.mapTransactionsResponseToTransactions(response)
                    if (!data.isNullOrEmpty()) {
                        emit(Resource.Success(data))
                    } else {
                        emit(Resource.Empty())
                    }
                }
            } else {
                emit(Resource.Error(message = "Something went wrong, can't get your transactions"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Something went wrong, can't get your transactions"))
        }
    }

}