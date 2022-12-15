package com.example.core.domain.interactor

import com.example.core.data.Resource
import com.example.core.domain.body.MakeOrderBody
import com.example.core.domain.model.*
import com.example.core.domain.repository.IRepository
import com.example.core.domain.usecase.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeInteractor @Inject constructor(private val repository: IRepository) : HomeUseCase {

    override fun getCityFromApi(): Flow<Resource<List<City>>> = repository.getCityFromApi()

    override fun insertCityToDb(listCity: List<City>) = repository.insertCityToDb(listCity)

    override fun getCityCount(): Flow<Resource<Int>> = repository.getCityCount()

    override fun getAllProducts(page: Int, size: Int): Flow<Resource<List<Product>>> =
        repository.getAllProducts(page, size)

    override fun getProductById(id: Int): Flow<Resource<Product>> = repository.getProductById(id)

    override fun getProductByCategory(
        page: Int,
        size: Int,
        category: String,
    ): Flow<Resource<List<Product>>> = repository.getProductByCategory(page, size, category)

    override fun getCart(token: String): Flow<Resource<List<Cart>>> = repository.getCart(token)

}