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
class ShippingInteractor @Inject constructor(private val repository: IRepository) :
    ShippingUseCase {

    override fun getCity(city: String, type: String): Flow<Resource<List<City>>> =
        repository.getCity(city, type)

    override fun getShippingCosts(
        destination: String,
        weight: String,
        courier: String
    ): Flow<Resource<List<Shipping>>> = repository.getShippingCosts(destination, weight, courier)

}