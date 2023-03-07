package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.domain.model.CartDetail
import com.example.core.domain.model.MakeOrder
import com.example.core.domain.body.MakeOrderBody
import com.example.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface CheckoutUseCase {

    fun getCheckedCart(token: String): Flow<Resource<List<Cart>>>

    fun getCartDetail(token: String, shippingCost: Int): Flow<Resource<CartDetail>>

    fun makeOrder(token: String, makeOrderBody: MakeOrderBody): Flow<Resource<MakeOrder>>

    fun getUser(token: String): Flow<Resource<User>>

    fun getToken(): Flow<String>

}