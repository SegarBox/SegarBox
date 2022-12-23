package com.example.core.domain.interactor

import androidx.paging.PagingData
import com.example.core.data.Resource
import com.example.core.data.source.remote.network.ApiServices
import com.example.core.domain.body.MakeOrderBody
import com.example.core.domain.body.UpdateStatusBody
import com.example.core.domain.model.*
import com.example.core.domain.repository.IRepository
import com.example.core.domain.usecase.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Interactor @Inject constructor(private val repository: IRepository) : AddressUseCase,
    CartUseCase, CheckoutUseCase, DetailUseCase, HomeUseCase, InvoiceUseCase, LoginUseCase,
    MapsUseCase, ProfileUseCase, RatingUseCase, RegisterUseCase, SearchUseCase, ShippingUseCase,
    TransactionUseCase {

    override fun getUserAddresses(token: String): Flow<Resource<List<Address>>> =
        repository.getUserAddresses(token)

    override fun deleteAddress(token: String, addressId: Int): Flow<Resource<String>> =
        repository.deleteAddress(token, addressId)

    override fun getCityFromApi(): Flow<Resource<List<City>>> =
        repository.getCityFromApi()

    override fun insertCityToDb(listCity: List<City>) =
        repository.insertCityToDb(listCity)

    override fun getCityCount(): Flow<Resource<Int>> =
        repository.getCityCount()

    override fun getAllProducts(page: Int, size: Int): Flow<Resource<List<Product>>> =
        repository.getAllProducts(page, size)

    override fun getProductById(id: Int): Flow<Resource<Product>> =
        repository.getProductById(id)

    override fun getProductByCategory(
        page: Int,
        size: Int,
        category: String,
    ): Flow<Resource<List<Product>>> =
        repository.getProductByCategory(page, size, category)

    override fun getRatings(token: String): Flow<Resource<List<Rating>>> =
        repository.getRatings(token)

    override fun getProductPaging(
        filter: String,
        filterValue: String,
    ): Flow<PagingData<Product>> =
        repository.getProductPaging(filter, filterValue)

    override fun getTransactions(token: String, status: String): Flow<Resource<List<Transaction>>> =
        repository.getTransactions(token, status)

    override fun saveRating(
        token: String,
        ratingId: Int,
        transactionId: Int,
        productId: Int,
        rating: Int,
    ): Flow<Resource<String>> =
        repository.saveRating(token, ratingId, transactionId, productId, rating)

    override fun logout(token: String): Flow<Resource<String>> =
        repository.logout(token)

    override fun addCart(token: String, productId: Int, productQty: Int): Flow<Resource<String>> =
        repository.addCart(token, productId, productQty)

    override fun deleteCart(token: String, cartId: Int): Flow<Resource<String>> =
        repository.deleteCart(token, cartId)

    override fun updateCart(
        token: String,
        cartId: Int,
        productId: Int,
        productQty: Int,
        isChecked: Int,
    ): Flow<Resource<String>> =
        repository.updateCart(token, cartId, productId, productQty, isChecked)

    override fun getCheckedCart(token: String): Flow<Resource<List<Cart>>> =
        repository.getCheckedCart(token)

    override fun getCartDetail(token: String, shippingCost: Int): Flow<Resource<CartDetail>> =
        repository.getCartDetail(token, shippingCost)

    override fun makeOrder(token: String, makeOrderBody: MakeOrderBody): Flow<Resource<MakeOrder>> =
        repository.makeOrder(token, makeOrderBody)

    override fun getTransactionById(
        token: String,
        transactionId: Int,
    ): Flow<Resource<Transaction>> =
        repository.getTransactionById(token, transactionId)

    override fun getUser(token: String): Flow<Resource<User>> =
        repository.getUser(token)

    override fun updateTransactionStatus(
        token: String,
        transactionId: Int,
        updateStatusBody: UpdateStatusBody,
    ): Flow<Resource<String>> =
        repository.updateTransactionStatus(token, transactionId, updateStatusBody)

    override fun login(email: String, password: String): Flow<Resource<Login>> =
        repository.login(email, password)

    override fun getAddress(latLng: String): Flow<Resource<List<Maps>>> =
        repository.getAddress(latLng)

    override fun saveAddress(
        token: String,
        street: String,
        city: String,
        postalCode: String,
    ): Flow<Resource<String>> =
        repository.saveAddress(token, street, city, postalCode)

    override fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        password_confirmation: String,
    ): Flow<Resource<Register>> =
        repository.register(name, email, phone, password, password_confirmation)

    override fun getCity(city: String, type: String): Flow<Resource<List<City>>> =
        repository.getCity(city, type)

    override fun getShippingCosts(
        destination: String,
        weight: String,
        courier: String,
    ): Flow<Resource<List<Shipping>>> =
        repository.getShippingCosts(destination, weight, courier)

    override fun getCart(token: String): Flow<Resource<List<Cart>>> =
        repository.getCart(token)

}