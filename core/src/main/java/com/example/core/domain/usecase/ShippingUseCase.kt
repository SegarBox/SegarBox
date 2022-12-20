package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.domain.model.City
import com.example.core.domain.model.Shipping
import kotlinx.coroutines.flow.Flow

interface ShippingUseCase {

    fun getCity(city: String, type: String): Flow<Resource<List<City>>>

    fun getShippingCosts(destination: String, weight: String, courier: String): Flow<Resource<List<Shipping>>>

}