package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.domain.body.UpdateStatusBody
import com.example.core.domain.model.Transaction
import com.example.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface InvoiceUseCase {

    fun getTransactionById(token: String, transactionId: Int): Flow<Resource<Transaction>>

    fun getUser(token: String): Flow<Resource<User>>

    fun updateTransactionStatus(
        token: String,
        transactionId: Int,
        updateStatusBody: UpdateStatusBody,
    ): Flow<Resource<String>>
    
}