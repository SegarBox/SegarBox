package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.domain.model.Cart
import com.example.core.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface DetailUseCase {

    fun getProductById(id: Int): Flow<Resource<Product>>

    fun getCart(token: String): Flow<Resource<List<Cart>>>

    fun addCart(token: String, productId: Int, productQty: Int): Flow<Resource<String>>

    fun updateCart(
        token: String,
        cartId: Int,
        productId: Int,
        productQty: Int,
        isChecked: Int,
    ): Flow<Resource<String>>

    fun deleteCart(token: String, cartId: Int): Flow<Resource<String>>

}