package com.example.core.domain.interactor

import com.example.core.data.Resource
import com.example.core.domain.body.MakeOrderBody
import com.example.core.domain.body.UpdateStatusBody
import com.example.core.domain.model.*
import com.example.core.domain.repository.IRepository
import com.example.core.domain.usecase.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapsInteractor @Inject constructor(private val repository: IRepository) : MapsUseCase {

    override fun getAddress(latLng: String): Flow<Resource<List<Maps>>> =
        repository.getAddress(latLng)

    override fun saveAddress(
        token: String,
        street: String,
        city: String,
        postalCode: String,
    ): Flow<Resource<String>> = repository.saveAddress(token, street, city, postalCode)

}