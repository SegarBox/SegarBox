package com.example.core.domain.interactor

import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.domain.model.Transaction
import com.example.core.domain.repository.IRepository
import com.example.core.domain.usecase.TransactionUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionInteractor @Inject constructor(private val repository: IRepository) :
    TransactionUseCase {

    override fun getCart(token: String): Flow<Resource<List<Cart>>> = repository.getCart(token)

    override fun getTransactions(token: String, status: String): Flow<Resource<List<Transaction>>> =
        repository.getTransactions(token, status)

}