package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ProfileUseCase {

    fun getUser(token: String): Flow<Resource<User>>

    fun getCart(token: String): Flow<Resource<List<Cart>>>

    fun logout(token: String): Flow<Resource<String>>

}