package com.example.core.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.core.data.Resource
import com.example.core.domain.body.MostPopularBody
import com.example.core.domain.model.Cart
import com.example.core.domain.model.City
import com.example.core.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

interface HomeUseCase {

    fun getCityFromApi(): Flow<Resource<List<City>>>

    fun insertCityToDb(listCity: List<City>)

    fun getCityCount(): Flow<Resource<Int>>

    fun getAllProducts(page: Int, size: Int): Flow<Resource<List<Product>>>

    fun getProductById(id: Int): Flow<Resource<Product>>

    fun getProductByCategory(page: Int, size: Int, category: String): Flow<Resource<List<Product>>>

    fun getProductByMostPopular(mostPopularBody: MostPopularBody): Flow<Resource<List<Product>>>

    fun getCart(token: String): Flow<Resource<List<Cart>>>

    fun getIntro(): Flow<Boolean>

    fun saveIntro(isAlreadyIntro: Boolean)

    fun getToken(): Flow<String>

    fun getTheme(): Flow<Boolean>

}