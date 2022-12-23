package com.example.core.domain.repository

import androidx.paging.PagingData
import com.example.core.data.Resource
import com.example.core.data.source.remote.network.ApiServices
import com.example.core.domain.body.MakeOrderBody
import com.example.core.domain.body.UpdateStatusBody
import com.example.core.domain.model.*
import kotlinx.coroutines.flow.Flow

interface IRepository {

    fun getUserAddresses(token: String): Flow<Resource<List<Address>>>

    fun deleteAddress(token: String, addressId: Int): Flow<Resource<String>>

    fun getCart(token: String): Flow<Resource<List<Cart>>>

    fun getCheckedCart(token: String): Flow<Resource<List<Cart>>>

    fun addCart(token: String, productId: Int, productQty: Int): Flow<Resource<String>>

    fun deleteCart(token: String, cartId: Int): Flow<Resource<String>>

    fun updateCart(token: String, cartId: Int, productId: Int, productQty: Int, isChecked: Int): Flow<Resource<String>>

    fun getCartDetail(token: String, shippingCost: Int): Flow<Resource<CartDetail>>

    fun makeOrder(token: String, makeOrderBody: MakeOrderBody): Flow<Resource<MakeOrder>>

    fun getProductById(id: Int): Flow<Resource<Product>>

    fun getCityFromApi(): Flow<Resource<List<City>>>

    fun insertCityToDb(listCity: List<City>)

    fun getCityCount(): Flow<Resource<Int>>

    fun getAllProducts(page: Int, size: Int): Flow<Resource<List<Product>>>

    fun getProductByCategory(page: Int, size: Int, category: String): Flow<Resource<List<Product>>>

    fun getTransactionById(token: String, transactionId: Int): Flow<Resource<Transaction>>

    fun getUser(token: String): Flow<Resource<User>>

    fun updateTransactionStatus(
        token: String,
        transactionId: Int,
        updateStatusBody: UpdateStatusBody,
    ): Flow<Resource<String>>

    fun login(email: String, password: String): Flow<Resource<Login>>

    fun getAddress(latLng: String): Flow<Resource<List<Maps>>>

    fun saveAddress(token: String, street: String, city: String, postalCode: String): Flow<Resource<String>>

    fun logout(token: String): Flow<Resource<String>>

    fun getRatings(token: String): Flow<Resource<List<Rating>>>

    fun saveRating(token: String, ratingId: Int, transactionId: Int, productId: Int, rating: Int): Flow<Resource<String>>

    fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        password_confirmation: String,
    ): Flow<Resource<Register>>

    fun getProductPaging(
        filter: String,
        filterValue: String,
    ): Flow<PagingData<Product>>

    fun getCity(city: String, type: String): Flow<Resource<List<City>>>

    fun getShippingCosts(destination: String, weight: String, courier: String): Flow<Resource<List<Shipping>>>

    fun getTransactions(token: String, status: String): Flow<Resource<List<Transaction>>>

}