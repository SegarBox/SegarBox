package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.domain.model.CartDetail
import kotlinx.coroutines.flow.Flow

interface CartUseCase {

    fun getCart(token: String): Flow<Resource<List<Cart>>>

    fun getCheckedCart(token: String): Flow<Resource<List<Cart>>>

    fun deleteCart(token: String, cartId: Int): Flow<Resource<String>>

    fun updateCart(token: String, cartId: Int, productId: Int, productQty: Int, isChecked: Int): Flow<Resource<String>>

    fun getCartDetail(token: String, shippingCost: Int): Flow<Resource<CartDetail>>

}