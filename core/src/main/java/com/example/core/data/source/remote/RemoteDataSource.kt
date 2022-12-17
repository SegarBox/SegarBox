package com.example.core.data.source.remote

import com.beust.klaxon.Klaxon
import com.example.core.data.Resource
import com.example.core.data.source.remote.network.FlaskApiServices
import com.example.core.data.source.remote.network.MapsApiServices
import com.example.core.data.source.remote.network.RajaOngkirApiServices
import com.example.core.data.source.remote.network.SegarBoxApiServices
import com.example.core.data.source.remote.response.UserCartResponse
import com.example.core.domain.model.Address
import com.example.core.domain.model.Cart
import com.example.core.domain.model.CartDetail
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
                    val address = DataMapper.mapGetAddressResponseToAddress(response)
                    if (!address.isNullOrEmpty()) {
                        emit(Resource.Success(address))
                    } else {
                        emit(Resource.Empty())
                    }
                }
            } else {
                emit(Resource.Error(message = "User address can't be accessed"))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "User address can't be accessed"))
        }
    }

    suspend fun deleteAddress(token: String, addressId: Int): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val request = segarBoxApiServices.deleteAddress(token, addressId)

            if (request.isSuccessful) {
                request.body()?.let { response ->
                    response.message?.let {
                        emit(Resource.Success(it))
                    }
                }
            } else {
                emit(Resource.Error(message = "Delete address is denied"))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(message = "Delete address is denied"))
        }
    }

    suspend fun getCart(token: String): Flow<Resource<List<Cart>>> = flow {
        emit(Resource.Loading())
        try {
            val request = segarBoxApiServices.getUserCart(token)

            if (request.isSuccessful) {
                request.body()?.let { response ->
                    val cart = DataMapper.mapCartResponseToCart(response)
                    if (!cart.isNullOrEmpty()) {
                        emit(Resource.Success(cart))
                    } else {
                        emit(Resource.Empty())
                    }
                }
            } else {
                val result = Klaxon().parse<UserCartResponse>(request.errorBody()!!.string())
                emit(Resource.Error(message = result!!.message.toString()))
            }

        } catch (ex: Exception) {
            emit(Resource.Error(message = "Cart can't be accessed"))
        }
    }

//    fun getCheckedCart(token: String): Flow<Resource<List<Cart>>>
//
//    fun deleteCart(token: String, cartId: Int): Flow<Resource<String>>
//
//    fun updateCart(token: String, cartId: Int, productId: Int, productQty: Int, isChecked: Int): Flow<Resource<String>>
//
//    fun getCartDetail(token: String, shippingCost: Int): Flow<Resource<CartDetail>>

}