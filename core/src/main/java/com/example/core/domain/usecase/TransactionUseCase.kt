package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionUseCase {

    fun getCart(token: String): Flow<Resource<List<Cart>>>

    fun getTransactions(token: String, status: String): Flow<Resource<List<Transaction>>>

}