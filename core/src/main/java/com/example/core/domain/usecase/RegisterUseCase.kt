package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.domain.model.Login
import com.example.core.domain.model.Register
import kotlinx.coroutines.flow.Flow

interface RegisterUseCase {

    fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        password_confirmation: String,
    ): Flow<Resource<Register>>

    fun saveToken(token: String)

    fun saveUserId(userId: Int)
}