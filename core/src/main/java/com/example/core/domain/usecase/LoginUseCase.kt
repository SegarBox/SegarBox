package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.domain.model.Login
import kotlinx.coroutines.flow.Flow

interface LoginUseCase {

    fun login(email: String, password: String): Flow<Resource<Login>>

}