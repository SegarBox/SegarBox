package com.example.core.domain.interactor

import com.example.core.data.Resource
import com.example.core.domain.model.Address
import com.example.core.domain.repository.IRepository
import com.example.core.domain.usecase.AddressUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressInteractor @Inject constructor(private val repository: IRepository) : AddressUseCase {

    override fun getUserAddresses(token: String): Flow<Resource<List<Address>>> =
        repository.getUserAddresses(token)

    override fun deleteAddress(token: String, addressId: Int): Flow<Resource<String>> =
        repository.deleteAddress(token, addressId)

}