package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.domain.model.Address
import kotlinx.coroutines.flow.Flow

interface AddressUseCase {

    fun getUserAddresses(token: String): Flow<Resource<List<Address>>>

    fun deleteAddress(token: String, addressId: Int): Flow<Resource<String>>

}