package com.example.core.data

import androidx.paging.PagingData
import androidx.paging.map
import com.example.core.data.source.local.LocalDataSource
import com.example.core.data.source.remote.RemoteDataSource
import com.example.core.data.source.remote.network.ApiServices
import com.example.core.domain.body.MakeOrderBody
import com.example.core.domain.body.MostPopularBody
import com.example.core.domain.body.UpdateStatusBody
import com.example.core.domain.model.*
import com.example.core.domain.repository.IRepository
import com.example.core.utils.DataMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val coroutineScope: CoroutineScope,
) : IRepository {

    override fun getUserAddresses(token: String): Flow<Resource<List<Address>>> = flow {
        emitAll(remoteDataSource.getUserAddresses(token))
    }.flowOn(Dispatchers.IO)

    override fun deleteAddress(token: String, addressId: Int): Flow<Resource<String>> = flow {
        emitAll(remoteDataSource.deleteAddress(token, addressId))
    }.flowOn(Dispatchers.IO)

    override fun getCart(token: String): Flow<Resource<List<Cart>>> = flow {
        emitAll(remoteDataSource.getCart(token))
    }.flowOn(Dispatchers.IO)

    override fun getCheckedCart(token: String): Flow<Resource<List<Cart>>> = flow {
        emitAll(remoteDataSource.getCheckedCart(token))
    }.flowOn(Dispatchers.IO)

    override fun addCart(token: String, productId: Int, productQty: Int): Flow<Resource<String>> =
        flow {
            emitAll(remoteDataSource.addCart(token, productId, productQty))
        }.flowOn(Dispatchers.IO)

    override fun deleteCart(token: String, cartId: Int): Flow<Resource<String>> = flow {
        emitAll(remoteDataSource.deleteCart(token, cartId))
    }.flowOn(Dispatchers.IO)

    override fun updateCart(
        token: String,
        cartId: Int,
        productId: Int,
        productQty: Int,
        isChecked: Int,
    ): Flow<Resource<String>> = flow {
        emitAll(remoteDataSource.updateCart(token, cartId, productId, productQty, isChecked))
    }.flowOn(Dispatchers.IO)

    override fun getCartDetail(token: String, shippingCost: Int): Flow<Resource<CartDetail>> =
        flow {
            emitAll(remoteDataSource.getCartDetail(token, shippingCost))
        }.flowOn(Dispatchers.IO)

    override fun makeOrder(token: String, makeOrderBody: MakeOrderBody): Flow<Resource<MakeOrder>> =
        flow {
            emitAll(remoteDataSource.makeOrder(token, makeOrderBody))
        }.flowOn(Dispatchers.IO)

    override fun getProductById(id: Int): Flow<Resource<Product>> = flow {
        emitAll(remoteDataSource.getProductById(id))
    }.flowOn(Dispatchers.IO)

    override fun getCityFromApi(): Flow<Resource<List<City>>> = flow {
        emitAll(remoteDataSource.getCityFromApi())
    }.flowOn(Dispatchers.IO)

    override fun getProductByMostPopular(mostPopularBody: MostPopularBody): Flow<Resource<List<Product>>> = flow {
        emitAll(remoteDataSource.getProductByMostPopular(mostPopularBody))
    }.flowOn(Dispatchers.IO)

    override fun getAllProducts(page: Int, size: Int): Flow<Resource<List<Product>>> = flow {
        emitAll(remoteDataSource.getAllProducts(page, size))
    }.flowOn(Dispatchers.IO)

    override fun getProductByCategory(
        page: Int,
        size: Int,
        category: String,
    ): Flow<Resource<List<Product>>> = flow {
        emitAll(remoteDataSource.getProductByCategory(page, size, category))
    }.flowOn(Dispatchers.IO)

    override fun getTransactionById(
        token: String,
        transactionId: Int,
    ): Flow<Resource<Transaction>> = flow {
        emitAll(remoteDataSource.getTransactionById(token, transactionId))
    }.flowOn(Dispatchers.IO)

    override fun getUser(token: String): Flow<Resource<User>> = flow {
        emitAll(remoteDataSource.getUser(token))
    }.flowOn(Dispatchers.IO)

    override fun updateTransactionStatus(
        token: String,
        transactionId: Int,
        updateStatusBody: UpdateStatusBody,
    ): Flow<Resource<String>> = flow {
        emitAll(remoteDataSource.updateTransactionStatus(token, transactionId, updateStatusBody))
    }.flowOn(Dispatchers.IO)

    override fun login(email: String, password: String): Flow<Resource<Login>> = flow {
        emitAll(remoteDataSource.login(email, password))
    }.flowOn(Dispatchers.IO)

    override fun getAddress(latLng: String): Flow<Resource<List<Maps>>> = flow {
        emitAll(remoteDataSource.getAddress(latLng))
    }.flowOn(Dispatchers.IO)

    override fun saveAddress(
        token: String,
        street: String,
        city: String,
        postalCode: String,
    ): Flow<Resource<String>> = flow {
        emitAll(remoteDataSource.saveAddress(token, street, city, postalCode))
    }.flowOn(Dispatchers.IO)

    override fun logout(token: String): Flow<Resource<String>> {
        TODO("Not yet implemented")
    }

    override fun getRatings(token: String): Flow<Resource<List<Rating>>> = flow {
        emitAll(remoteDataSource.getRatings(token))
    }.flowOn(Dispatchers.IO)

    override fun saveRating(
        token: String,
        ratingId: Int,
        transactionId: Int,
        productId: Int,
        rating: Int,
    ): Flow<Resource<String>> = flow {
        emitAll(remoteDataSource.saveRating(token, ratingId, transactionId, productId, rating))
    }.flowOn(Dispatchers.IO)

    override fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        password_confirmation: String,
    ): Flow<Resource<Register>> = flow {
        emitAll(remoteDataSource.register(name, email, phone, password, password_confirmation))
    }.flowOn(Dispatchers.IO)

    override fun getProductPaging(
        filter: String,
        filterValue: String,
    ): Flow<PagingData<Product>> =
        remoteDataSource.getProductPaging(filter, filterValue).map { pagingData ->
            pagingData.map {
                DataMapper.mapProductItemToProduct(it)
            }
        }

    override fun getShippingCosts(
        destination: String,
        weight: String,
        courier: String,
    ): Flow<Resource<List<Shipping>>> = flow {
        emitAll(remoteDataSource.getShippingCosts(destination, weight, courier))
    }.flowOn(Dispatchers.IO)

    override fun getTransactions(token: String, status: String): Flow<Resource<List<Transaction>>> =
        flow {
            emitAll(remoteDataSource.getTransactions(token, status))
        }.flowOn(Dispatchers.IO)

    override fun getToken(): Flow<String> =
        localDataSource.getToken()

    override fun getIntro(): Flow<Boolean> =
        localDataSource.getIntro()

    override fun saveIntro(isAlreadyIntro: Boolean) {
        coroutineScope.launch {
            localDataSource.saveIntro(isAlreadyIntro)
        }
    }

    override fun getCity(city: String, type: String): Flow<Resource<List<City>>> =
        localDataSource.getCity(city, type)

    override fun insertCityToDb(listCity: List<City>) {
        coroutineScope.launch {
            localDataSource.insertCityToDb(listCity)
        }
    }

    override fun getCityCount(): Flow<Resource<Int>> =
        localDataSource.getCityCount()

}