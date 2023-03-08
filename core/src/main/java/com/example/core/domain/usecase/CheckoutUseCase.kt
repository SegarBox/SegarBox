package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.domain.body.MakeOrderBody
import com.example.core.domain.model.*
import kotlinx.coroutines.flow.Flow

interface CheckoutUseCase {

    fun getCheckedCart(token: String): Flow<Resource<List<Cart>>>

    fun getCartDetail(token: String, shippingCost: Int): Flow<Resource<CartDetail>>

    fun makeOrder(token: String, makeOrderBody: MakeOrderBody): Flow<Resource<MakeOrder>>

    fun getTransactionById(token: String, transactionId: Int): Flow<Resource<Transaction>>

    fun getUser(token: String): Flow<Resource<User>>

    fun getToken(): Flow<String>

}