package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.domain.model.Maps
import kotlinx.coroutines.flow.Flow

interface MapsUseCase {

    fun getAddress(latLng: String): Flow<Resource<List<Maps>>>

    fun saveAddress(token: String, street: String, city: String, postalCode: String): Flow<Resource<String>>

    fun getToken(): Flow<String>

}