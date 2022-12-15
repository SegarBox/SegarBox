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
class ProfileInteractor @Inject constructor(private val repository: IRepository) : ProfileUseCase {

    override fun getUser(token: String): Flow<Resource<User>> = repository.getUser(token)

    override fun getCart(token: String): Flow<Resource<List<Cart>>> = repository.getCart(token)

    override fun logout(token: String) = repository.logout(token)

}