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
class InvoiceInteractor @Inject constructor(private val repository: IRepository) : InvoiceUseCase {

    override fun getTransactionById(
        token: String,
        transactionId: Int,
    ): Flow<Resource<Transaction>> = repository.getTransactionById(token, transactionId)

    override fun getUser(token: String): Flow<Resource<User>> = repository.getUser(token)

    override fun updateTransactionStatus(
        token: String,
        transactionId: Int,
        updateStatusBody: UpdateStatusBody,
    ): Flow<Resource<String>> =
        repository.updateTransactionStatus(token, transactionId, updateStatusBody)

}